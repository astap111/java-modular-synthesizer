package com.synthesizer.javafx.form;


import javafx.scene.control.Button;

public abstract class Key extends Button {
    protected double noteFrequency;
    protected int keyBinded;
    protected int aNoteFrequency = 440;
    protected double noteMultiplier = Math.pow(2, 1.0 / 12);
    int WD = 29;
    int HT = 196;

    public double getNoteFrequency() {
        return noteFrequency;
    }

    public int getKeyBinded() {
        return keyBinded;
    }
}
