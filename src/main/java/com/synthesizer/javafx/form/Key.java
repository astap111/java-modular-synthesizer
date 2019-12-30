package com.synthesizer.javafx.form;


import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

public abstract class Key extends Button {
    protected double noteFrequency;
    protected KeyCode keyBinded;
    protected int aNoteFrequency = 440;
    protected double noteMultiplier = Math.pow(2, 1.0 / 12);
    int WD = 29;
    int HT = 196;

    public double getNoteFrequency() {
        return noteFrequency;
    }

    public KeyCode getKeyBinded() {
        return keyBinded;
    }
}
