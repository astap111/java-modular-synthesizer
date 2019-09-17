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
        for (int i = 0; i < output.length; i++) {
            output[i] = i * 2 / period - 1;
        }
    }
}
