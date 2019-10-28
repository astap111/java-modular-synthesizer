package com.synthesizer.channel.generator;

import com.synthesizer.channel.Channel;

import static com.synthesizer.SimpleSynth.*;

public abstract class Generator implements Channel {
    protected volatile double[] output;
    protected double volume;
    protected double frequency;
    private double step = 0;

    public Generator() {
    }

    public Generator(double volume) {
        this.volume = volume;
    }

    @Override
    public double[] readData() {
        output = new double[SAMPLES];
        genetateWave(output);
        return output;
    }

    protected double nextStep() {
        double period = (double) SAMPLE_RATE / frequency; //in samples
        step = step % period;
        return step++;
    }

    protected abstract void genetateWave(double[] output);

    @Override
    public double getVolume() {
        return volume;
    }

    @Override
    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public void setFrequency(double frequency) {
        step = step * this.frequency / frequency;
        this.frequency = frequency;
    }

    @Override
    public double getFrequency() {
        return frequency;
    }

    public double[] getLastData() {
        return output;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    @Override
    public void attack() {

    }

    @Override
    public void release() {

    }
}
