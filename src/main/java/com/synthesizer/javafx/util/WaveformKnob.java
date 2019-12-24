package com.synthesizer.javafx.util;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polyline;

public enum WaveformKnob implements KnobContent {
    SINE(CubicCurve.class, 0, 0, 10, -20, 10, 20, 20, 0),
    SQUARE(Polyline.class, 0, 0, 0, -5, 10, -5, 10, 5, 20, 5, 20, 0),
    PULSE(Polyline.class, 0, 0, 0, -5, 6, -5, 6, 5, 20, 5, 20, 0),
    TRIANGLE(Polyline.class, 0, 0, 5, -10, 10, 0),
    RAMP(Polyline.class, 0, 0, 10, -10, 10, 0),
    SAWTOOTH(Polyline.class, 0, 0, 0, -10, 10, 0);

    private final Class shapeClass;
    private final double[] params;

    WaveformKnob(Class shapeClass, double... params) {
        this.shapeClass = shapeClass;
        this.params = params;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public Node getGraphic() {
        if (shapeClass.equals(Polyline.class)) {
            return new Polyline(params);
        } else {
            return new CubicCurve(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7]) {
                {
                    setFill(Color.TRANSPARENT);
                    setStroke(Color.BLACK);
                }
            };
        }
    }
}
