package com.synthesizer.channel;

import static com.synthesizer.SimpleSynth.SAMPLE_RATE;

public class SquareWave extends Generator {
    public SquareWave(double volume) {
        super(volume);
    }

    @Override
    protected void genetateWave(double[] output, int bufferNumber) {
        double period = (double) SAMPLE_RATE / frequency;
        double halfPeriod = period / 2;
        double bufferStart = bufferNumber * output.length;
        double waveStart = Math.floor(bufferStart / period) * period;

        for (int i = 0; i < output.length; i++) {
            double pointer = i + bufferStart - waveStart;
            if (pointer >= 0 && pointer < halfPeriod) {
                output[i] = 1;
            } else if (pointer >= halfPeriod && pointer < halfPeriod * 2) {
                output[i] = -1;
            } else {
                waveStart += period;
                output[i] = 1;
            }
        }
    }
}
