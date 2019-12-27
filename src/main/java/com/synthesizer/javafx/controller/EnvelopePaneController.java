package com.synthesizer.javafx.controller;

import com.synthesizer.channel.processor.ADSREnvelope;
import com.synthesizer.javafx.control.knob.Knob;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

public class EnvelopePaneController implements Initializable {
    @FXML
    private Knob envelopeDecay;
    @FXML
    private Knob envelopeRelease;
    @FXML
    private Knob envelopeAttack;
    @FXML
    private Slider envelopeSustain;

    private ADSREnvelope adsrEnvelope;
    private GrandMotherController grandMotherController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adsrEnvelope = new ADSREnvelope(envelopeAttack.getValue(), envelopeDecay.getValue(), envelopeSustain.getValue(), envelopeRelease.getValue());
        envelopeDecay.valueProperty().addListener((observable, oldValue, newValue) -> {
            adsrEnvelope.setDecay(newValue.doubleValue());
        });
        envelopeRelease.valueProperty().addListener((observable, oldValue, newValue) -> {
            adsrEnvelope.setRelease(newValue.doubleValue());
        });
        envelopeAttack.valueProperty().addListener((observable, oldValue, newValue) -> {
            adsrEnvelope.setAttack(newValue.doubleValue());
        });
        envelopeSustain.valueProperty().addListener((observable, oldValue, newValue) -> {
            adsrEnvelope.setSustain(newValue.doubleValue());
        });
    }

    public void postInitialize(GrandMotherController grandMotherController) {
        this.grandMotherController = grandMotherController;
        this.grandMotherController.getMixer().addVolumeEnvelope(adsrEnvelope);
    }

    public ADSREnvelope getAdsrEnvelope() {
        return adsrEnvelope;
    }
}
