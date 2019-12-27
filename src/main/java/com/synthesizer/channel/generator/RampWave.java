package com.synthesizer.channel.generator;

import static com.synthesizer.SimpleSynth.SAMPLE_RATE;

public class RampWave extends Generator {
    public RampWave() {
        super();
    }

    public RampWave(double volume) {
        super(volume);
    }

    @Override
    protected void genetateWave(double[] output) {
        double period = (double) SAMPLE_RATE / frequency;
        double angle = 2 / period;

        for (int i = 0; i < output.length; i++) {
            output[i] = angle * nextStep() - 1;
        }
    }
}
