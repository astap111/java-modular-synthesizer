package com.synthesizer.form;

import javax.swing.*;

public abstract class Key extends JButton {
    protected double note;
    protected int keyBinded;
    protected int aNoteFrequency = 440;
    protected double noteMultiplier = Math.pow(2, 1.0 / 12);
    // change WD to suit your screen
    int WD = 40;
    int HT = (WD * 9) / 2;

    public double getNote() {
        return note;
    }

    public int getKeyBinded() {
        return keyBinded;
    }
}
