package com.synthesizer.javafx.controller;

import com.synthesizer.javafx.control.discreteknob.DiscreteKnob;
import com.synthesizer.javafx.util.Waveform;
import javafx.fxml.FXML;

import java.util.Arrays;

import static com.synthesizer.javafx.util.Waveform.*;

public class GrandMotherController {

    @FXML
    private DiscreteKnob modulationWaveform;

    public void initialize() {
        modulationWaveform.setValues(Arrays.asList(new Waveform[]{SINE, SAWTOOTH, RAMP, SQUARE}));
        modulationWaveform.setValue(Waveform.SAWTOOTH);
    }
}
