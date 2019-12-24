package com.synthesizer.javafx.util;

import javafx.scene.Node;

public enum OctaveKnob implements KnobContent {
    HALF("2'"),
    QUATER("4'"),
    EIGHTS("8'"),
    SIXTEENTH("16'"),
    THIRTYSECONDTH("32'");

    OctaveKnob(String text) {
        this.text = text;
    }

    private String text;

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public Node getGraphic() {
        return null;
    }
}
