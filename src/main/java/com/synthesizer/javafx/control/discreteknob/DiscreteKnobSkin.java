package com.synthesizer.javafx.control.discreteknob;

import com.synthesizer.javafx.util.KnobContent;
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

public class DiscreteKnobSkin extends SkinBase<DiscreteKnob> {
    private boolean invalidSkin = true;
    private StackPane stackPane;
    private Group group;
    private static final double SCALE_MIN = -80;
    private static final double SCALE_MAX = 80;
    private int ticksCount;
    private double anglePerTick;


    public DiscreteKnobSkin(DiscreteKnob control) {
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

        Line thumb = new Line(0, -radius + 3, 0, -radius * 2 / 3 - 1);
        thumb.setStroke(Color.WHITE);
        thumb.setStrokeWidth(2);

        Circle rotatable = new Circle(radius);
        rotatable.setOpacity(0);
        Circle clickable = new Circle(radius);
        clickable.setOpacity(0);

        ticksCount = getSkinnable().getValues().size();
        anglePerTick = (SCALE_MAX - SCALE_MIN) / (ticksCount - 1);

        group = new Group(thumb, rotatable);
        int tickIndex = getSkinnable().getValues().indexOf(getSkinnable().getValue());
        tickIndex = tickIndex < 0 ? 0 : tickIndex;
        group.setRotate(SCALE_MIN + anglePerTick * tickIndex);

        Label label;
        if (getSkinnable().getValue() != null) {
            label = new Label(getSkinnable().getValue().getText(), getSkinnable().getValue().getGraphic());
        } else {
            label = new Label();
        }
        label.setFont(Font.font(radius / 2));

        clickable.setOnMouseDragged(event -> {
            double angle = getAngle(event);
            for (int i = 0; i < ticksCount; i++) {
                if (Math.abs(SCALE_MIN + i * anglePerTick - angle) <= anglePerTick / 2) {
                    group.setRotate(SCALE_MIN + i * anglePerTick);
                    KnobContent knobContent = getSkinnable().getValues().get(i);
                    getSkinnable().setValue(knobContent);
                    label.setText(knobContent.getText());
                    label.setGraphic(knobContent.getGraphic());
                    break;
                }
            }
        });

        stackPane = new StackPane();
        stackPane.getChildren().clear();
        stackPane.getChildren().addAll(outerCircle, innerCircle, label, group, clickable);
        getChildren().clear();
        getChildren().add(stackPane);
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
