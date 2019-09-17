package com.synthesizer.channel;

public abstract class Generator implements Channel {
    protected volatile double[] output;

    protected double volume;
    protected double frequency;
    protected volatile boolean isPlaying;

    public Generator(double volume) {
        this.volume = volume;
    }

    @Override
    public double getVolume() {
        return volume;
    }

    @Override
    public void setVolume(double volume) {
        this.volume = volume;
    }

    public void startPlaying(double frequency) {
        this.frequency = frequency;
        isPlaying = true;
    }

    public void stopPlaying() {
        isPlaying = false;
    }

    public double[] getLastData() {
        return output;
    }
}
