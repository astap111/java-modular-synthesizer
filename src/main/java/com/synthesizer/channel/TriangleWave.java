package com.synthesizer.channel;

import static com.synthesizer.SimpleSynth.SAMPLE_LENGTH;
import static com.synthesizer.SimpleSynth.SAMPLE_RATE;

public class TriangleWave extends Generator {
    public TriangleWave(double volume) {
        super(volume);
    }

    @Override
    public double[] readData(int bufferNumber) {
        int samples = (SAMPLE_LENGTH * SAMPLE_RATE) / 1000;
        output = new double[samples];

        if (isPlaying) {
            genetateTriangleWave(output, bufferNumber);
        } else if (frequency != 0) {
            genetateTriangleWave(output, bufferNumber);
            for (int i = 0; i < output.length; i++) {
                frequency = 0;
                if (i > 0 && output[i] * output[i - 1] <= 0) {
                    output[i] = 0;
                    return output;
                }
            }
        }

        return output;
    }

    private void genetateTriangleWave(double[] output, int bufferNumber) {
        double period = (double) SAMPLE_RATE / frequency;
        double quaterPeriod = period / 4;
        double angle = 1 / quaterPeriod;
        double bufferStart = bufferNumber * output.length;
        double waveStart = (int) (bufferStart / period) * period;

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
            }
        }
    }
}
