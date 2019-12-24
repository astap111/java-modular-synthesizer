package com.synthesizer.javafx.controller;

import com.synthesizer.javafx.control.discreteknob.DiscreteKnob;
import com.synthesizer.javafx.util.OctaveKnob;
import com.synthesizer.javafx.util.WaveformKnob;
import javafx.fxml.FXML;

import java.util.Arrays;

import static com.synthesizer.javafx.util.WaveformKnob.*;
import static com.synthesizer.javafx.util.OctaveKnob.*;

public class GrandMotherController {
    @FXML
    private DiscreteKnob modulationWaveform;
    @FXML
    private DiscreteKnob oscillator1Waveform;
    @FXML
    private DiscreteKnob oscillator2Waveform;
    @FXML
    private DiscreteKnob oscillator1Octave;
    @FXML
    private DiscreteKnob oscillator2Octave;

    public void initialize() {
        modulationWaveform.setValues(Arrays.asList(new WaveformKnob[]{SINE, SAWTOOTH, RAMP, SQUARE}));
        modulationWaveform.setValue(SAWTOOTH);
        oscillator1Waveform.setValues(Arrays.asList(new WaveformKnob[]{TRIANGLE, SAWTOOTH, SQUARE, PULSE}));
        oscillator1Waveform.setValue(SAWTOOTH);
        oscillator2Waveform.setValues(Arrays.asList(new WaveformKnob[]{TRIANGLE, SAWTOOTH, SQUARE, PULSE}));
        oscillator2Waveform.setValue(SQUARE);
        oscillator1Octave.setValues(Arrays.asList(new OctaveKnob[]{QUATER, EIGHTS, SIXTEENTH, THIRTYSECONDTH}));
        oscillator1Octave.setValue(QUATER);
        oscillator2Octave.setValues(Arrays.asList(new OctaveKnob[]{HALF, QUATER, EIGHTS, SIXTEENTH}));
        oscillator2Octave.setValue(HALF);
    }
}
