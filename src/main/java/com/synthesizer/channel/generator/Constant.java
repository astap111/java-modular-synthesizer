package com.synthesizer.channel.generator;

public class Constant extends Generator {
    public Constant() {
        super();
    }

    public Constant(double volume) {
        super(volume);
    }

    @Override
    protected void genetateWave(double[] output) {
        for (int i = 0; i < output.length; i++) {
            output[i] = volume;
        }
    }
}
