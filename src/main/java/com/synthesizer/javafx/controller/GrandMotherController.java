package com.synthesizer.javafx.controller;

import com.synthesizer.channel.Channel;
import com.synthesizer.channel.processor.*;
import com.synthesizer.javafx.control.knob.Knob;
import com.synthesizer.javafx.form.Key;
import com.synthesizer.javafx.form.KeyboardPane;
import com.synthesizer.javafx.util.AudioByteConverter;
import com.synthesizer.javafx.util.EventListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class GrandMotherController implements Initializable, EventListener {
    @FXML
    private KeyboardPane keyboardPane;
    @FXML
    private MixerPaneController mixerPaneController;
    @FXML
    private OscillatorsPaneController oscillatorsPaneController;
    @FXML
    private EnvelopePaneController envelopePaneController;
    @FXML
    private ModulationPaneController modulationPaneController;
    @FXML
    private FilterPaneController filterPaneController;
    @FXML
    private ChartsPaneController chartsPaneController;
    @FXML
    private PresetsPaneController presetsPaneController;
    @FXML
    private Knob outputVolume;

    private volatile double currentFrequency;
    private GrandmotherMixer mixer;
    private Compressor compressor;
    private Equalizer lpfChannel;
    private Limiter outputLimiter;
    private Channel rootChannel;
    private AudioByteConverter audioByteConverter;

    public void initialize(URL location, ResourceBundle resources) {
        mixer = new GrandmotherMixer();
        compressor = new Compressor(mixer);
        lpfChannel = filterPaneController.getLpf();
        lpfChannel.addChannel(compressor);
        lpfChannel.setEnabled(true);
        outputLimiter = new Limiter(lpfChannel);
        rootChannel = outputLimiter;

        mixerPaneController.postInitialize(this, oscillatorsPaneController);
        oscillatorsPaneController.postInitialize(this, mixerPaneController);
        envelopePaneController.postInitialize(this, filterPaneController);
        chartsPaneController.postInitialize(oscillatorsPaneController);
        modulationPaneController.postInitialize(oscillatorsPaneController);
        presetsPaneController.postInitialize(this, envelopePaneController, oscillatorsPaneController, mixerPaneController, filterPaneController);

        outputLimiter.setVolume(outputVolume.getValue() / 100);
        outputVolume.valueProperty().addListener((observable, oldValue, newValue) -> {
            outputLimiter.setVolume(newValue.doubleValue() / 100);
        });

        audioByteConverter = new AudioByteConverter();
        audioByteConverter.addChangeListener(this);
        audioByteConverter.addChannel(rootChannel);
        rootChannel.setFrequency(440);

        initializeKeyboardPane();
    }

    private void initializeKeyboardPane() {
        for (Key key : keyboardPane.getKeys()) {
            key.pressedProperty().addListener((observable, wasPressed, pressed) -> {
                if (pressed) {
                    attack(key);
                } else {
                    release(key);
                }
            });
            key.setOnKeyPressed(event -> {
                Key pressedKey = keyboardPane.getKey(event.getCode());
                if (pressedKey != null) {
                    attack(pressedKey);
                }
            });
            key.setOnKeyReleased(event -> {
                Key releasedKey = keyboardPane.getKey(event.getCode());
                if (releasedKey != null) {
                    release(releasedKey);
                }
            });
        }
    }

    private void release(Key key) {
        if (currentFrequency == 0 || currentFrequency == key.getNoteFrequency()) {
            envelopePaneController.getAdsrEnvelope().release();
            currentFrequency = 0;
        }
    }

    private void attack(Key key) {
        if (currentFrequency == 0) {
            envelopePaneController.getAdsrEnvelope().attack();
        }
        rootChannel.setFrequency(key.getNoteFrequency());
        currentFrequency = key.getNoteFrequency();
    }

    public AudioByteConverter getAudioByteConverter() {
        return this.audioByteConverter;
    }

    public GrandmotherMixer getMixer() {
        return mixer;
    }

    @Override
    public void fireEvent() {
        double[] values = audioByteConverter.getLastData();
        chartsPaneController.setData(values);
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

    public Knob getOutputVolume() {
        return outputVolume;
    }
}
