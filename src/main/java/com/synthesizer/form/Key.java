package com.synthesizer.form;

import javax.swing.*;

public abstract class Key extends JButton {
    protected double noteFrequency;
    protected int keyBinded;
    protected int aNoteFrequency = 440;
    protected int octave = 0;
    protected double noteMultiplier = Math.pow(2, 1.0 / 12);
    // change WD to suit your screen
    int WD = 20;
    int HT = (WD * 9) / 2;

    public double getNoteFrequency() {
        return noteFrequency * Math.pow(2, octave);
    }

    public int getKeyBinded() {
        return keyBinded;
    }

    public int getOctave() {
        return octave;
    }

    public void setOctave(int octave) {
        this.octave = octave;
    }
}
