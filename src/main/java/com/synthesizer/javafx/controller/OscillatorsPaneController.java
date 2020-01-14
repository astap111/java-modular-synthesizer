package com.synthesizer.javafx.controller;

import com.synthesizer.channel.generator.*;
import com.synthesizer.channel.processor.Detuner;
import com.synthesizer.channel.processor.Octaver;
import com.synthesizer.javafx.control.discreteknob.DiscreteKnob;
import com.synthesizer.javafx.control.knob.Knob;
import com.synthesizer.javafx.util.OctaveKnob;
import com.synthesizer.javafx.util.WaveformKnob;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import static com.synthesizer.javafx.util.OctaveKnob.*;
import static com.synthesizer.javafx.util.OctaveKnob.HALF;
import static com.synthesizer.javafx.util.WaveformKnob.*;
import static com.synthesizer.javafx.util.WaveformKnob.SQUARE;

public class OscillatorsPaneController implements Initializable {
    @FXML
    private DiscreteKnob oscillator1Waveform;
    @FXML
    private DiscreteKnob oscillator2Waveform;
    @FXML
    private DiscreteKnob oscillator1Octave;
    @FXML
    private DiscreteKnob oscillator2Octave;
    @FXML
    private Knob detuneFrequency;

    private MixerPaneController mixerPaneController;
    private GrandMotherController grandMotherController;

    private volatile Generator oscillator1;
    private volatile Generator oscillator2;
    private Octaver octaver1;
    private Octaver octaver2;
    private Detuner detuner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        oscillator1Waveform.setValues(Arrays.asList(new WaveformKnob[]{TRIANGLE, SAWTOOTH, SQUARE, PULSE}));
        oscillator1Waveform.setValue(SAWTOOTH);
        oscillator2Waveform.setValues(Arrays.asList(new WaveformKnob[]{TRIANGLE, SAWTOOTH, SQUARE, PULSE}));
        oscillator2Waveform.setValue(SQUARE);
        oscillator1Octave.setValues(Arrays.asList(new OctaveKnob[]{THIRTYSECONDTH, SIXTEENTH, EIGHTS, QUATER}));
        oscillator1Octave.setValue(QUATER);
        oscillator2Octave.setValues(Arrays.asList(new OctaveKnob[]{SIXTEENTH, EIGHTS, QUATER, HALF}));
        oscillator2Octave.setValue(HALF);
    }

    public void postInitialize(GrandMotherController grandMotherController, MixerPaneController mixerPaneController) {
        this.grandMotherController = grandMotherController;
        this.mixerPaneController = mixerPaneController;

        oscillator1 = new SawtoothWave(this.mixerPaneController.getOscillator1Volume().getValue() / 100);
        oscillator2 = new SquareWave(this.mixerPaneController.getOscillator2Volume().getValue() / 100);
        octaver1 = new Octaver(oscillator1, QUATER);
        octaver2 = new Octaver(oscillator2, HALF);
        detuner = new Detuner(octaver2);
        this.grandMotherController.getMixer().addChannel(octaver1);
        this.grandMotherController.getMixer().addChannel(detuner);

        oscillator1Waveform.valueProperty().addListener((observable, oldValue, newValue) -> {
            double osc1Volume = this.mixerPaneController.getOscillator1Volume().getValue() / 100;
//            double frequency = oscillator1.getFrequency();
            switch ((WaveformKnob) newValue) {
                case TRIANGLE:
                    oscillator1 = new TriangleWave(osc1Volume);
                    break;
                case SAWTOOTH:
                    oscillator1 = new SawtoothWave(osc1Volume);
                    break;
                case SQUARE:
                    oscillator1 = new SquareWave(osc1Volume);
                    break;
                case PULSE:
                    oscillator1 = new PulseWave(osc1Volume);
                    break;
            }
            octaver1.addChannel(oscillator1);
            octaver1.setFrequency(mixerPaneController.getNoise().getFrequency());
            this.grandMotherController.getMixer().resetStepValues();
        });

        oscillator2Waveform.valueProperty().addListener((observable, oldValue, newValue) -> {
            double osc2Volume = this.mixerPaneController.getOscillator2Volume().getValue() / 100;
//            double frequency = oscillator2.getFrequency();
            switch ((WaveformKnob) newValue) {
                case TRIANGLE:
                    oscillator2 = new TriangleWave(osc2Volume);
                    break;
                case SAWTOOTH:
                    oscillator2 = new SawtoothWave(osc2Volume);
                    break;
                case SQUARE:
                    oscillator2 = new SquareWave(osc2Volume);
                    break;
                case PULSE:
                    oscillator2 = new PulseWave(osc2Volume);
                    break;
            }
            octaver2.addChannel(oscillator2);
            octaver2.setFrequency(mixerPaneController.getNoise().getFrequency());
            this.grandMotherController.getMixer().resetStepValues();
        });

        oscillator1Octave.valueProperty().addListener((observable, oldValue, newValue) -> {
            octaver1.setOctaveFactor(((OctaveKnob) newValue).getOctaveFactor());
            octaver1.setFrequency(mixerPaneController.getNoise().getFrequency());
            this.grandMotherController.getMixer().resetStepValues();
        });

        oscillator2Octave.valueProperty().addListener((observable, oldValue, newValue) -> {
            octaver2.setOctaveFactor(((OctaveKnob) newValue).getOctaveFactor());
            octaver2.setFrequency(mixerPaneController.getNoise().getFrequency());
            this.grandMotherController.getMixer().resetStepValues();
        });

        detuneFrequency.valueProperty().addListener((observable, oldValue, newValue) -> {
            detuner.setDetuneFactor((Double) newValue);
        });
    }

    public Generator getOscillator1() {
        return oscillator1;
    }

    public void setOscillator1(Generator oscillator1) {
        this.oscillator1 = oscillator1;
    }

    public Generator getOscillator2() {
        return oscillator2;
    }

    public void setOscillator2(Generator oscillator2) {
        this.oscillator2 = oscillator2;
    }

    public DiscreteKnob getOscillator1Waveform() {
        return oscillator1Waveform;
    }

    public DiscreteKnob getOscillator2Waveform() {
        return oscillator2Waveform;
    }

    public DiscreteKnob getOscillator1Octave() {
        return oscillator1Octave;
    }

    public DiscreteKnob getOscillator2Octave() {
        return oscillator2Octave;
    }
}
