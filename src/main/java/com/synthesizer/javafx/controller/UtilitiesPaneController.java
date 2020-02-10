package com.synthesizer.javafx.controller;

import com.synthesizer.javafx.control.knob.Knob;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class UtilitiesPaneController implements Initializable {
    @FXML
    private Knob attenuator;
    @FXML
    private Knob highPass;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
