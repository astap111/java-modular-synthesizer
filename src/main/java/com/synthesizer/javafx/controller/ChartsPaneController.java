package com.synthesizer.javafx.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polyline;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import static com.synthesizer.javafx.util.AudioConstants.SAMPLES;
import static com.synthesizer.javafx.util.AudioConstants.SAMPLE_RATE;

public class ChartsPaneController implements Initializable {
    @FXML
    private Polyline chartLine;
    private double prefHeight;
    private OscillatorsPaneController oscillatorsPaneController;
    private volatile int counter = 0;
    private List<Double> buffer = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chartLine.getPoints().clear();
        for (int i = 0; i < SAMPLES; i++) {
            chartLine.getPoints().add((double) i);
            chartLine.getPoints().add(0.0);
        }
        prefHeight = ((StackPane) chartLine.getParent()).getPrefHeight() - 20;
    }


    public void postInitialize(OscillatorsPaneController oscillatorsPaneController) {
        this.oscillatorsPaneController = oscillatorsPaneController;
    }

    public synchronized void setData(double[] values) {
        for (double v : values) {
            synchronized (this) {
                buffer.add(v);
            }
        }
        counter++;
        if (counter > 20) {
            double frequency = oscillatorsPaneController.getOscillator1().getFrequency();
            double currentStep = oscillatorsPaneController.getOscillator1().getStep();
            if (frequency == 0.0) {
                return;
            }
            double waveLength = SAMPLE_RATE / frequency;
            List<Double> subList = buffer.subList((int) (buffer.size() - currentStep - waveLength), (int) (buffer.size() - currentStep));
            updateChart(new ArrayList<>(subList));
            synchronized (this) {
                buffer.clear();
            }
            counter = 0;
        }
    }

    private void updateChart(List<Double> values) {
        Platform.runLater(() -> {
            chartLine.getPoints().clear();
            double xChartMultiplier = (double) SAMPLES / values.size();
            double yChartMultiplier = Math.abs(1 / values.stream().max(Comparator.comparingDouble(Math::abs)).get());
            for (int i = 0; i < values.size(); i++) {
                chartLine.getPoints().add(i * xChartMultiplier);
                chartLine.getPoints().add(-values.get(i) * prefHeight * yChartMultiplier / 2 + prefHeight / 2);
            }
        });
    }
}
