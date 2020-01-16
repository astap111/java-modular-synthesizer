package com.synthesizer.javafx.controller;

import com.synthesizer.channel.generator.Noise;
import com.synthesizer.javafx.control.knob.Knob;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MixerPaneController implements Initializable {
    @FXML
    private Knob oscillator1Volume;
    @FXML
    private Knob oscillator2Volume;
    @FXML
    private Knob noiseVolume;
    private Noise noise;

    private OscillatorsPaneController oscillatorsPaneController;
    private GrandMotherController grandMotherController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void postInitialize(GrandMotherController grandMotherController, OscillatorsPaneController oscillatorsPaneController) {
        this.oscillatorsPaneController = oscillatorsPaneController;
        this.grandMotherController = grandMotherController;

        noise = new Noise(noiseVolume.getValue() / 100);
        this.grandMotherController.getMixer().setNoise(noise);
        this.grandMotherController.getMixer().addChannel(noise);

        oscillator1Volume.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.oscillatorsPaneController.getOscillator1().setVolume(newValue.doubleValue() / 100);
        });
        oscillator2Volume.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.oscillatorsPaneController.getOscillator2().setVolume(newValue.doubleValue() / 100);
        });
        noiseVolume.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.noise.setVolume(newValue.doubleValue() / 100);
        });
    }

    public Knob getOscillator1Volume() {
        return oscillator1Volume;
    }

    public void setOscillator1Volume(Knob oscillator1Volume) {
        this.oscillator1Volume = oscillator1Volume;
    }

    public Knob getOscillator2Volume() {
        return oscillator2Volume;
    }

    public void setOscillator2Volume(Knob oscillator2Volume) {
        this.oscillator2Volume = oscillator2Volume;
    }

    public Knob getNoiseVolume() {
        return noiseVolume;
    }

    public void setNoiseVolume(Knob noiseVolume) {
        this.noiseVolume = noiseVolume;
    }

    public Noise getNoise() {
        return noise;
    }
}
