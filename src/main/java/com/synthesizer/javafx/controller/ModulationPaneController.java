package com.synthesizer.javafx.controller;

import com.synthesizer.javafx.control.discreteknob.DiscreteKnob;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modulationWaveform.setValues(Arrays.asList(new WaveformKnob[]{SINE, SAWTOOTH, RAMP, SQUARE}));
        modulationWaveform.setValue(SAWTOOTH);
    }
}
