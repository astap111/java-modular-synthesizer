package com.synthesizer.channel.generator;

import static com.synthesizer.swing.SimpleSynth.SAMPLE_RATE;

public class TriangleWave extends Generator {
    public TriangleWave() {
        super();
    }

    public TriangleWave(double volume) {
        super(volume);
    }

    @Override
    protected void genetateWave(double[] output) {
        double period = (double) SAMPLE_RATE / frequency;
        double quaterPeriod = period / 4;
        double angle = 1 / quaterPeriod;

        for (int i = 0; i < output.length; i++) {
            double step = nextStep();

            if (step >= 0 && step < quaterPeriod) {
                output[i] = angle * step;
            } else if (step >= quaterPeriod && step < quaterPeriod * 3) {
                output[i] = -angle * step + 2;
            } else if (step >= quaterPeriod * 3 && step < quaterPeriod * 4) {
                output[i] = angle * step - 4;
            }
        }
    }
}
