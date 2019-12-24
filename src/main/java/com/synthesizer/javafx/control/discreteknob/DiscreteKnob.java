package com.synthesizer.javafx.control.discreteknob;

import com.synthesizer.javafx.util.KnobContent;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.util.List;

public class DiscreteKnob extends Control {
    private SimpleObjectProperty<KnobContent> value = new SimpleObjectProperty<>();
    private SimpleDoubleProperty radius = new SimpleDoubleProperty();
    private List<KnobContent> values;

    public List<KnobContent> getValues() {
        return values;
    }

    public void setValues(List<KnobContent> values) {
        this.values = values;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DiscreteKnobSkin(this);
    }

    public KnobContent getValue() {
        return value.get();
    }

    public SimpleObjectProperty<KnobContent> valueProperty() {
        return value;
    }

    public void setValue(KnobContent value) {
        this.value.set(value);
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
