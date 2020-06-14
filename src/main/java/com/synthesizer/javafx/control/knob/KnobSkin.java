package com.synthesizer.javafx.control.knob;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class KnobSkin extends SkinBase<Knob> {
    private boolean invalidSkin = true;
    private StackPane stackPane;
    private Group group;
    private static final double SCALE_MIN = -140;
    private static final double SCALE_MAX = 140;


    public KnobSkin(Knob control) {
        super(control);
        control.widthProperty().addListener(observable -> invalidSkin = true);
        control.heightProperty().addListener(observable -> invalidSkin = true);
    }

    public void drawKnob() {
        double radius = getSkinnable().getRadius();

        Circle outerCircle = new Circle(radius, Color.BLACK);
        outerCircle.setStroke(Color.WHITE);
        outerCircle.setStrokeWidth(1.5);
        Circle innerCircle = new Circle(radius * 2 / 3);
        RadialGradient radialGradient = new RadialGradient(0, 0,
                0.5, 0.5, 0.5, true, null,
                new Stop(0, Color.LIGHTGREY), new Stop(1, Color.GREY));
        innerCircle.setFill(radialGradient);
        innerCircle.setStroke(Color.WHITE);
        innerCircle.setStrokeWidth(1.5);
        Label label = new Label(String.format("%.0f", getSkinnable().getValue()));
        label.setFont(Font.font(radius / 2));
        Line line = new Line(0, -radius + 3, 0, -radius * 2 / 3 - 1);
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(2);

        Circle rotatable = new Circle(radius);
        rotatable.setOpacity(0);
        Circle clickable = new Circle(radius);
        clickable.setOpacity(0);

        group = new Group(line, rotatable);
        double initialRotateRate;
        if (getSkinnable().isLogScale()) {
            initialRotateRate = (log(getSkinnable().getValue()) - log(getSkinnable().getMin()))
                    / (log(getSkinnable().getMax()) - log(getSkinnable().getMin()));
        } else {
            initialRotateRate = (getSkinnable().getValue() - getSkinnable().getMin())
                    / (getSkinnable().getMax() - getSkinnable().getMin());
        }
        group.setRotate(SCALE_MIN + initialRotateRate * (SCALE_MAX - SCALE_MIN));

        group.rotateProperty().addListener((observable, oldValue, newValue) -> {
            double angle = newValue.doubleValue();
            double rate = (angle - SCALE_MIN) / (SCALE_MAX - SCALE_MIN);
            double value;
            if (getSkinnable().isLogScale()) {
                double range = log(getSkinnable().getMax()) - log(getSkinnable().getMin());
                double logValue = rate * range + log(getSkinnable().getMin());
                value = Math.pow(Math.E, logValue);
            } else {
                double range = (getSkinnable().getMax() - getSkinnable().getMin());
                value = rate * range + getSkinnable().getMin();
            }
            getSkinnable().setValue(value);
            label.setText(String.format("%.0f", value));
        });

        getSkinnable().valueProperty().addListener((observable, oldValue, newValue) -> {
            double rate;
            if (getSkinnable().isLogScale()) {
                double range = log(getSkinnable().getMax()) - log(getSkinnable().getMin());
                rate = (log(newValue.doubleValue()) - log(getSkinnable().getMin())) / range;
            } else {
                double range = getSkinnable().getMax() - getSkinnable().getMin();
                rate = (newValue.doubleValue() - getSkinnable().getMin()) / range;
            }
            double angle = rate * (SCALE_MAX - SCALE_MIN) + SCALE_MIN;
            group.setRotate(angle);
        });

        clickable.setOnMouseDragged(event -> {
            double angle = getAngle(event);
            group.setRotate(angle);
        });
        stackPane = new StackPane();
        stackPane.getChildren().clear();
        stackPane.getChildren().addAll(outerCircle, innerCircle, label, group, clickable);
        getChildren().clear();
        getChildren().add(stackPane);
    }

    private double log(double value) {
        return value <= 0 ? 0 : Math.log(value);
    }

    private double getAngle(MouseEvent event) {
        double x = event.getX();
        double y = -event.getY();
        double angle;
        if (x == 0) {
            if (y < 0) {
                angle = 180;
            } else {
                angle = 0;
            }
        } else if (y == 0) {
            if (x < 0) {
                angle = -90;
            } else {
                angle = 90;
            }
        } else {
            angle = Math.atan(x / y) * 180 / Math.PI;
            if (y < 0) {
                if (x > 0) {
                    angle += 180;
                } else {
                    angle -= 180;
                }
            }
        }
        if (angle > SCALE_MAX) {
            angle = SCALE_MAX;
        } else if (angle < SCALE_MIN) {
            angle = SCALE_MIN;
        }

        return angle;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        if (invalidSkin) {
            drawKnob();
            invalidSkin = false;
        }
        layoutInArea(stackPane, contentX, contentY, contentWidth, contentHeight, -1, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + bottomInset + 30;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return rightInset + leftInset + 30;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 20 + topInset + bottomInset;
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 20 + rightInset + leftInset;
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }
}
