package com.synthesizer.channel;

import static com.synthesizer.SimpleSynth.SAMPLE_RATE;

public class SawtoothWave extends Generator {
    public SawtoothWave(double volume) {
        super(volume);
    }

    @Override
    protected void genetateWave(double[] output, int bufferNumber) {
        double period = (double) SAMPLE_RATE / frequency;
        double angle = 2 / period;
        double bufferStart = bufferNumber * output.length;
        double waveStart = (int) Math.floor(bufferStart / period) * period;

        for (int i = 0; i < output.length; i++) {
            double pointer = i + bufferStart - waveStart;
            if (pointer >= 0 && pointer < period) {
                output[i] = angle * pointer - 1;
            } else {
                waveStart += period;
                output[i] = angle * pointer - 3;
            }
        }
    }
}
