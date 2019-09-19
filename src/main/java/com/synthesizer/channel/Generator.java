package com.synthesizer.channel;

import static com.synthesizer.SimpleSynth.*;

public abstract class Generator implements Channel {
    protected volatile double[] output;

    protected double volume;
    protected double frequency;
    protected volatile boolean isPlaying;

    public Generator(double volume) {
        this.volume = volume;
    }

    @Override
    public double[] readData(int bufferNumber) {
        int samples = SAMPLES;
        output = new double[samples];

        if (isPlaying) {
            genetateWave(output, bufferNumber);
        } else if (frequency != 0) {
            genetateWave(output, bufferNumber);
            for (int i = 0; i < output.length; i++) {
                frequency = 0;
                if (i > 0 && output[i] * output[i - 1] <= 0) {
                    output[i] = 0;
                    return output;
                }
            }
        }

        return output;
    }

    protected abstract void genetateWave(double[] output, int bufferNumber);

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
