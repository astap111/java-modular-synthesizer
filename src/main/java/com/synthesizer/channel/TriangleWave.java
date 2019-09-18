package com.synthesizer.channel;

import static com.synthesizer.SimpleSynth.SAMPLE_RATE;

public class TriangleWave extends Generator {
    public TriangleWave(double volume) {
        super(volume);
    }

    @Override
    protected void genetateWave(double[] output, int bufferNumber) {
        double period = (double) SAMPLE_RATE / frequency;
        double quaterPeriod = period / 4;
        double angle = 1 / quaterPeriod;
        double bufferStart = bufferNumber * output.length;
        double waveStart = (int) Math.floor(bufferStart / period) * period;

        for (int i = 0; i < output.length; i++) {
            double pointer = i + bufferStart - waveStart;
            if (pointer >= 0 && pointer < quaterPeriod) {
                output[i] = angle * pointer;
            } else if (pointer >= quaterPeriod && pointer < quaterPeriod * 3) {
                output[i] = -angle * pointer + 2;
            } else if (pointer >= quaterPeriod * 3 && pointer < quaterPeriod * 4) {
                output[i] = angle * pointer - 4;
            } else {
                waveStart += period;
                output[i] = angle * pointer - 4;
            }
        }
    }
}
