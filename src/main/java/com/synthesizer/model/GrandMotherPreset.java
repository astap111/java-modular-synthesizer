package com.synthesizer.model;

public class GrandMotherPreset {
    public String name;
    public double outputVolume;
    public double envelopeAttack;
    public double envelopeDecay;
    public double envelopeSustain;
    public double envelopeRelease;
    public String oscillator1Waveform;
    public String oscillator2Waveform;
    public String oscillator1Octave;
    public String oscillator2Octave;
    public double detuneFrequency;
    public boolean oscillatorSync;
    public double oscillator1Volume;
    public double oscillator2Volume;
    public double noiseVolume;
    public double filterCutoff;
    public double filterEnvelopeAmt;
    public double filterResonance;
    public double kbdTrack;

    @Override
    public String toString() {
        return name;
    }
}
