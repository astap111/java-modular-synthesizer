package com.synthesizer.javafx.controller;

import com.synthesizer.javafx.util.BiQuadraticFilter;
import com.synthesizer.channel.processor.Equalizer;
import com.synthesizer.javafx.control.knob.Knob;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class FilterPaneController implements Initializable {
    @FXML
    private Knob filterEnvelopeAmt;
    @FXML
    private Knob filterResonance;
    @FXML
    private Knob filterCutoff;

    private Equalizer lpf;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lpf = new Equalizer(null, BiQuadraticFilter.FilterType.LOWPASS, filterCutoff.getValue(), filterResonance.getValue(), 20, filterEnvelopeAmt.getValue());
        filterCutoff.valueProperty().addListener((observable, oldValue, newValue) -> {
            lpf.setCutOffFrequency(newValue.doubleValue());
        });
        filterResonance.valueProperty().addListener((observable, oldValue, newValue) -> {
            lpf.setResonance(newValue.doubleValue());
        });
        filterEnvelopeAmt.valueProperty().addListener((observable, oldValue, newValue) -> {
            lpf.setCutoffEnvelopeDepth(newValue.doubleValue());
        });
    }

    public Equalizer getLpf() {
        return lpf;
    }
}
