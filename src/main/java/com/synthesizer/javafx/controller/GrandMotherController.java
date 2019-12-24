package com.synthesizer.javafx.controller;

import com.synthesizer.javafx.control.discreteknob.DiscreteKnob;
import com.synthesizer.javafx.util.Waveform;
import javafx.fxml.FXML;

import java.util.Arrays;

public class GrandMotherController {

    @FXML
    private DiscreteKnob modulationWaveform;

    public void initialize() {
        modulationWaveform.setValues(Arrays.asList(Waveform.values()));
        modulationWaveform.setValue(Waveform.SAWTOOTH);
    }
}
