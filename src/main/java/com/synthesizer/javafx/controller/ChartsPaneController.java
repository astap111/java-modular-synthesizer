package com.synthesizer.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polyline;

import java.net.URL;
import java.util.ResourceBundle;

import static com.synthesizer.javafx.util.AudioConstants.SAMPLES;

public class ChartsPaneController implements Initializable {
    @FXML
    private Polyline chartLine;
    private double prefHeight;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chartLine.getPoints().clear();
        for (int i = 0; i < SAMPLES; i++) {
            chartLine.getPoints().add((double) i);
            chartLine.getPoints().add(0.0);
        }
        prefHeight = ((StackPane) chartLine.getParent()).getPrefHeight();
    }

    public void setData(double[] values) {
        for (int i = 0; i < values.length; i++) {
            chartLine.getPoints().set(i * 2 + 1, values[i] * prefHeight / 2 - prefHeight / 2);
        }
    }
}
