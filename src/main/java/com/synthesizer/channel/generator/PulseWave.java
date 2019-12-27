package com.synthesizer.channel.generator;

import static com.synthesizer.swing.SimpleSynth.SAMPLE_RATE;

public class PulseWave extends Generator {
    public PulseWave() {
        super();
    }

    public PulseWave(double volume) {
        super(volume);
    }

    @Override
    protected void genetateWave(double[] output) {
        double period = (double) SAMPLE_RATE / frequency;
        double pulsePeriod = period / 3;

        for (int i = 0; i < output.length; i++) {
            double step = nextStep();
            if (step >= 0 && step < pulsePeriod) {
                output[i] = 1;
            } else if (step >= pulsePeriod && step < period) {
                output[i] = -1;
            }
        }
    }
}
