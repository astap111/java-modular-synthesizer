package com.synthesizer.javafx.controller;

import com.synthesizer.channel.generator.ADSREnvelope;
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
        envelopeAttack.valueProperty().addListener((observable, oldValue, newValue) -> {
            adsrEnvelope.setAttack(newValue.doubleValue());
        });
        envelopeDecay.valueProperty().addListener((observable, oldValue, newValue) -> {
            adsrEnvelope.setDecay(newValue.doubleValue());
        });
        envelopeRelease.valueProperty().addListener((observable, oldValue, newValue) -> {
            adsrEnvelope.setRelease(newValue.doubleValue());
        });
        envelopeSustain.valueProperty().addListener((observable, oldValue, newValue) -> {
            adsrEnvelope.setSustain(newValue.doubleValue());
        });
    }

    public void postInitialize(GrandMotherController grandMotherController, FilterPaneController filterPaneController) {
        this.grandMotherController = grandMotherController;
        this.grandMotherController.getMixer().addVolumeEnvelope(adsrEnvelope);
        filterPaneController.getLpf().addCutoffEnvelope(adsrEnvelope);
    }

    public ADSREnvelope getAdsrEnvelope() {
        return adsrEnvelope;
    }

    public Knob getEnvelopeDecay() {
        return envelopeDecay;
    }

    public Knob getEnvelopeRelease() {
        return envelopeRelease;
    }

    public Knob getEnvelopeAttack() {
        return envelopeAttack;
    }

    public Slider getEnvelopeSustain() {
        return envelopeSustain;
    }
}
