package com.synthesizer.javafx.util;

import javafx.scene.Node;

public enum OctaveKnob implements KnobContent {
    HALF("2'", 2),
    QUATER("4'", 4),
    EIGHTS("8'", 8),
    SIXTEENTH("16'", 16),
    THIRTYSECONDTH("32'", 32);

    OctaveKnob(String text, double octaveFactor) {
        this.text = text;
        this.octaveFactor = octaveFactor;
    }

    private String text;
    private double octaveFactor;

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public Node getGraphic() {
        return null;
    }

    public double getOctaveFactor() {
        return octaveFactor;
    }
}
