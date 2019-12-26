package com.synthesizer.javafx.form;


import javafx.scene.control.Button;

public abstract class Key extends Button {
    protected double noteFrequency;
    protected int keyBinded;
    protected int aNoteFrequency = 440;
    protected int octave = 0;
    protected double noteMultiplier = Math.pow(2, 1.0 / 12);
    int WD = 29;
    int HT = 196;

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
