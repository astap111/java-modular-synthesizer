package com.synthesizer.javafx.control.roundknob;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class RoundKnob extends Control {
    private SimpleDoubleProperty value = new SimpleDoubleProperty();
    private SimpleDoubleProperty min = new SimpleDoubleProperty();
    private SimpleDoubleProperty max = new SimpleDoubleProperty();
    private SimpleDoubleProperty radius = new SimpleDoubleProperty();

    public RoundKnob() {
        setMin(0);
        setMax(100);
        setValue(0);
    }

    public double getValue() {
        return value.get();
    }

    public SimpleDoubleProperty valueProperty() {
        return value;
    }

    public void setValue(double value) {
        this.value.set(value);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RoundKnobSkin(this);
    }

    public double getMin() {
        return min.get();
    }

    public SimpleDoubleProperty minProperty() {
        return min;
    }

    public void setMin(double min) {
        this.min.set(min);
    }

    public double getMax() {
        return max.get();
    }

    public SimpleDoubleProperty maxProperty() {
        return max;
    }

    public void setMax(double max) {
        this.max.set(max);
    }

    public double getRadius() {
        return radius.get();
    }

    public SimpleDoubleProperty radiusProperty() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius.set(radius);
    }
}
