package com.synthesizer.channel.generator;

import java.util.Random;

public class Noise extends Generator {
    private Random random = new Random();

    public Noise() {
        super();
    }

    public Noise(double volume) {
        super(volume);
    }

    @Override
    protected void genetateWave(double[] output) {
        for (int i = 0; i < output.length; i++) {
            output[i] = random.nextGaussian();
        }
    }
}
