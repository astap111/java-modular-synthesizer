package com.synthesizer.javafx.controller;

import com.synthesizer.channel.Channel;
import com.synthesizer.channel.processor.Limiter;
import com.synthesizer.channel.processor.Mixer;
import com.synthesizer.javafx.control.discreteknob.DiscreteKnob;
import com.synthesizer.javafx.form.Key;
import com.synthesizer.javafx.form.KeyboardPane;
import com.synthesizer.javafx.util.AudioByteConverter;
import com.synthesizer.javafx.util.EventListener;
import com.synthesizer.javafx.util.WaveformKnob;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static com.synthesizer.javafx.util.WaveformKnob.*;

public class GrandMotherController implements Initializable, EventListener {
    @FXML
    private DiscreteKnob modulationWaveform;
    @FXML
    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> series = new XYChart.Series<>();

    @FXML
    private KeyboardPane keyboardPane;
    @FXML
    private MixerPaneController mixerPaneController;
    @FXML
    private OscillatorsPaneController oscillatorsPaneController;
    @FXML
    private EnvelopePaneController envelopePaneController;

    private volatile int counter;


    private volatile double currentFrequency;
    private Mixer mixer = new Mixer();
    private Limiter mixerLimiter = new Limiter(mixer);
    private Channel rootChannel = mixerLimiter;
    private AudioByteConverter audioByteConverter;

    public void initialize(URL location, ResourceBundle resources) {
        mixerPaneController.postInitialize(this, oscillatorsPaneController);
        oscillatorsPaneController.postInitialize(this, mixerPaneController);
        envelopePaneController.postInitialize(this);

        modulationWaveform.setValues(Arrays.asList(new WaveformKnob[]{SINE, SAWTOOTH, RAMP, SQUARE}));
        modulationWaveform.setValue(SAWTOOTH);

        audioByteConverter = new AudioByteConverter();
        audioByteConverter.addChangeListener(this);
        audioByteConverter.addChannel(rootChannel);
        rootChannel.setFrequency(440);

        initializeKeyboardPane();

        lineChart.getData().add(series);
    }

    private void initializeKeyboardPane() {
        for (Key key : keyboardPane.getKeys()) {
            key.setOnKeyPressed(event -> {
                Key pressedKey = keyboardPane.getKey(event.getCode());
                if (pressedKey != null) {
                    if (currentFrequency == 0) {
                        envelopePaneController.getAdsrEnvelope().attack();
                    }
                    rootChannel.setFrequency(pressedKey.getNoteFrequency());
                    currentFrequency = pressedKey.getNoteFrequency();
                }
            });
            key.setOnKeyReleased(event -> {
                Key releasedKey = keyboardPane.getKey(event.getCode());
                if (releasedKey != null) {
                    if (currentFrequency == 0 || currentFrequency == releasedKey.getNoteFrequency()) {
                        envelopePaneController.getAdsrEnvelope().release();
                        currentFrequency = 0;
                    }
                }
            });
        }
    }

    public AudioByteConverter getAudioByteConverter() {
        return this.audioByteConverter;
    }

    public Mixer getMixer() {
        return mixer;
    }

    @Override
    public void fireEvent() {
        counter++;
        if (counter > 50) {
            counter = 0;
            Platform.runLater(() -> {
                double[] values = audioByteConverter.getLastData();
                series.getData().clear();
                List<LineChart.Data<Number, Number>> data = new ArrayList<>();
                for (int i = 0; i < values.length; i++) {
                    data.add(new LineChart.Data<>(i, values[i]));
                }
                series.getData().addAll(data);
            });
        }
    }

    public void setScene(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            Key key = keyboardPane.getKey(event.getCode());
            if (key != null) {
                key.fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED,
                        key.getLayoutX(), key.getLayoutY(), key.getLayoutX(), key.getLayoutY(), MouseButton.PRIMARY, 1,
                        false, false, false, false, true, false, false, true, true, true, null));
            }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            Key key = keyboardPane.getKey(event.getCode());
            if (key != null) {
                key.fireEvent(new MouseEvent(MouseEvent.MOUSE_RELEASED,
                        key.getLayoutX(), key.getLayoutY(), key.getLayoutX(), key.getLayoutY(), MouseButton.PRIMARY, 1,
                        false, false, false, false, true, false, false, true, true, true, null));
            }
        });
    }
}
