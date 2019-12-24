package com.synthesizer.javafx.util;

import javafx.scene.Node;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;

public enum Waveform implements KnobContent {
    SINE(new CubicCurve(0, 0,
            5, -10,
            5, 10,
            10, 0)),
    SQUARE(new Polyline(0, 0,
            0, -5,
            10, 0,
            0, 10,
            10, 0,
            0, 5)),
    PULSE(new Polyline(0, 0,
            0, -5,
            10, 0,
            0, 10,
            10, 0,
            0, 5)),
    TRIANGLE(new Polyline(0, 0,
            5, -10,
            10, 0)),
    RAMP(new Polyline(0, 0,
            10, -10,
            10, 0)),
    SAWTOOTH(new Polyline(0, 0,
            0, -10,
            10, 0));

    private final Shape shape;

    Waveform(Shape shape) {
        this.shape = shape;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public Node getGraphic() {
        return shape;
    }
}
