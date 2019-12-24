package com.synthesizer.javafx.controller;

import com.synthesizer.javafx.control.discreteknob.DiscreteKnob;
import com.synthesizer.javafx.util.Waveform;
import javafx.fxml.FXML;

import java.util.Arrays;

import static com.synthesizer.javafx.util.Waveform.*;

public class GrandMotherController {

    @FXML
    private DiscreteKnob modulationWaveform;

    @FXML
    private DiscreteKnob oscillator1Waveform;

    @FXML
    private DiscreteKnob oscillator2Waveform;

    public void initialize() {
        modulationWaveform.setValues(Arrays.asList(new Waveform[]{SINE, SAWTOOTH, RAMP, SQUARE}));
        modulationWaveform.setValue(SAWTOOTH);
        oscillator1Waveform.setValues(Arrays.asList(new Waveform[]{TRIANGLE, SAWTOOTH, SQUARE, PULSE}));
        oscillator1Waveform.setValue(SAWTOOTH);
        oscillator2Waveform.setValues(Arrays.asList(new Waveform[]{TRIANGLE, SAWTOOTH, SQUARE, PULSE}));
        oscillator2Waveform.setValue(SQUARE);
    }
}
