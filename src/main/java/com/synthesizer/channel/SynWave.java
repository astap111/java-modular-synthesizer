package com.synthesizer.channel;

import static com.synthesizer.SimpleSynth.SAMPLE_RATE;
import static com.synthesizer.SimpleSynth.SAMPLE_LENGTH;

public class SynWave implements Generator {
    private double volume;
    private double frequency;
    private boolean isPlaying;

    @Override
    public double[] getData(int bufferNumber) {
        int samples = (SAMPLE_LENGTH * SAMPLE_RATE) / 1000;
        double[] output = new double[samples];

        if (isPlaying) {
            genetateSinWave(output, bufferNumber);
        } else if (!isPlaying && frequency != 0) {
            genetateSinWave(output, bufferNumber);
            for (int i = 0; i < output.length; i++) {
                if (i > 0 && output[i] * output[i - 1] <= 0) {
                    output[i] = 0;
                    frequency = 0;
                    return output;
                }
            }
        }

        return output;
    }

    double[] genetateSinWave(double[] output, int bufferNumber) {
        double period = (double) SAMPLE_RATE / frequency;
        for (int i = 0; i < output.length; i++) {
            double angle = 2.0 * Math.PI * (i + bufferNumber * output.length) / period;
            output[i] = Math.sin(angle);
        }
        return output;
    }

    @Override
    public double getVolume() {
        return volume;
    }

    @Override
    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public void startPlaying(double frequency) {
        this.frequency = frequency;
        isPlaying = true;
    }

    @Override
    public void stopPlaying() {
        isPlaying = false;
    }
}
