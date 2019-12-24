package com.synthesizer.javafx.util;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;

public enum Waveform implements KnobContent {
    SINE(new CubicCurve(0, 0,
            10, -20,
            10, 20,
            20, 0) {
        {
            setFill(Color.TRANSPARENT);
            setStroke(Color.BLACK);
        }
    }),
    SQUARE(new Polyline(0, 0,
            0, -5,
            10, -5,
            10, 5,
            20, 5,
            20, 0)),
    PULSE(new Polyline(0, 0,
            0, -5,
            6, -5,
            6, 5,
            20, 5,
            20, 0)),
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
