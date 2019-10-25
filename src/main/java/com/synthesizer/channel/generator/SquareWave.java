package com.synthesizer.channel.generator;

import static com.synthesizer.SimpleSynth.SAMPLE_RATE;

public class SquareWave extends Generator {
    public SquareWave(double volume) {
        super(volume);
    }

    @Override
    protected void genetateWave(double[] output) {
        double period = (double) SAMPLE_RATE / frequency;
        double halfPeriod = period / 2;

        for (int i = 0; i < output.length; i++) {
            double step = nextStep();
            if (step >= 0 && step < halfPeriod) {
                output[i] = 1;
            } else if (step >= halfPeriod && step < period) {
                output[i] = -1;
            }
        }
    }
}
