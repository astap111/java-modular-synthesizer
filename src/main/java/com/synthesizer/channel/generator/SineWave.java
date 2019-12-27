package com.synthesizer.channel.generator;

import static com.synthesizer.swing.SimpleSynth.SAMPLE_RATE;

public class SineWave extends Generator {
    public SineWave() {
        super();
    }

    public SineWave(double volume) {
        super(volume);
    }

    @Override
    protected void genetateWave(double[] output) {
        double period = (double) SAMPLE_RATE / frequency;
        for (int i = 0; i < output.length; i++) {
            double angle = 2.0 * Math.PI * nextStep() / period;
            output[i] = Math.sin(angle);
        }
    }
}
