package com.synthesizer.channel;

import static com.synthesizer.SimpleSynth.SAMPLE_RATE;
import static com.synthesizer.SimpleSynth.SAMPLE_LENGTH;

public class SynWave extends Generator {
    public SynWave(double volume) {
        super(volume);
    }

    @Override
    protected void genetateWave(double[] output, int bufferNumber) {
        double period = (double) SAMPLE_RATE / frequency;
        for (int i = 0; i < output.length; i++) {
            double angle = 2.0 * Math.PI * (i + bufferNumber * output.length) / period;
            output[i] = Math.sin(angle);
        }
    }
}
