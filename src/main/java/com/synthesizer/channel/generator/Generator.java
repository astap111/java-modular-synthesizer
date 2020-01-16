package com.synthesizer.channel.generator;

import com.synthesizer.channel.Channel;

import static com.synthesizer.swing.SimpleSynth.SAMPLES;
import static com.synthesizer.swing.SimpleSynth.SAMPLE_RATE;

public abstract class Generator implements Channel {
    protected volatile double[] output;
    protected double volume;
    protected double frequency;
    private double step = 0;
    private double syncPeriod;

    public Generator() {
    }

    public Generator(double volume) {
        this.volume = volume;
    }

    @Override
    public void addChannel(Channel channel) {
    }

    @Override
    public double[] readData() {
        this.syncPeriod = 0;
        output = new double[SAMPLES];
        genetateWave(output);
        return output;
    }

    public double[] readData(double syncPeriod) {
        this.syncPeriod = syncPeriod;
        output = new double[SAMPLES];
        genetateWave(output);
        return output;
    }

    protected double nextStep() {
        double period = (double) SAMPLE_RATE / frequency; //in samples
        if (syncPeriod == 0) {
            step = step % period;
            return step++;
        } else {
            step = step % syncPeriod;
            return step++ % period;
        }
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

    @Override
    public double getStep() {
        return step;
    }

    @Override
    public void setStep(double step) {
        this.step = step;
    }

    @Override
    public void attack() {

    }

    @Override
    public void release() {

    }

    @Override
    public String toString() {
        return String.format("%s{volume=%.0f, frequency=%.0f}", getClass().getSimpleName(), volume, frequency);
    }
}
