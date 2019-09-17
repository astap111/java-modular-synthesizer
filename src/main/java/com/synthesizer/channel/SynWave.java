package com.synthesizer.channel;

import static com.synthesizer.SimpleSynth.SAMPLE_RATE;
import static com.synthesizer.SimpleSynth.SAMPLE_LENGTH;

public class SynWave extends Generator {
    public SynWave(double volume) {
        super(volume);
    }

    @Override
    public double[] readData(int bufferNumber) {
        int samples = (SAMPLE_LENGTH * SAMPLE_RATE) / 1000;
        output = new double[samples];

        if (isPlaying) {
            genetateSinWave(output, bufferNumber);
        } else if (frequency != 0) {
            genetateSinWave(output, bufferNumber);
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

    private void genetateSinWave(double[] output, int bufferNumber) {
        double period = (double) SAMPLE_RATE / frequency;
        for (int i = 0; i < output.length; i++) {
            double angle = 2.0 * Math.PI * (i + bufferNumber * output.length) / period;
            output[i] = Math.sin(angle);
        }
    }
}
