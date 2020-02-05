package com.synthesizer.javafx.controller;

import com.synthesizer.channel.generator.PulseWave;
import com.synthesizer.javafx.control.discreteknob.DiscreteKnob;
import com.synthesizer.javafx.control.knob.Knob;
import com.synthesizer.javafx.util.WaveformKnob;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import static com.synthesizer.javafx.util.WaveformKnob.*;

public class ModulationPaneController implements Initializable {
    @FXML
    private DiscreteKnob modulationWaveform;
    @FXML
    private Knob modulationPulseWidthAmt;
    private OscillatorsPaneController oscillatorsPaneController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modulationWaveform.setValues(Arrays.asList(new WaveformKnob[]{SINE, SAWTOOTH, RAMP, SQUARE}));
        modulationWaveform.setValue(SAWTOOTH);
        modulationPulseWidthAmt.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (oscillatorsPaneController.getOscillator1() instanceof PulseWave) {
                ((PulseWave) oscillatorsPaneController.getOscillator1()).setPulseWidth(newValue.doubleValue() / 100);
            }
            if (oscillatorsPaneController.getOscillator2() instanceof PulseWave) {
                ((PulseWave) oscillatorsPaneController.getOscillator2()).setPulseWidth(newValue.doubleValue() / 100);
            }
        });
    }

    public void postInitialize(OscillatorsPaneController oscillatorsPaneController) {
        this.oscillatorsPaneController = oscillatorsPaneController;
    }
}
