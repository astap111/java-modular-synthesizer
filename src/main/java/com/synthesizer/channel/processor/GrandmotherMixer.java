package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;

import static com.synthesizer.swing.SimpleSynth.SAMPLES;

public class GrandmotherMixer extends Mixer {

    private volatile boolean syncOscillator2to1 = false;

    public GrandmotherMixer() {
    }

    @Override
    public double[] readData() {
        double[] result = new double[SAMPLES];
        double[] volumeEnvelopeData = volumeEnvelope.readData();
        if (resetStepValue) {
            for (Channel c : channels) {
                c.setStep(0);
            }
            resetStepValue = false;
        }
        for (Channel channel : channels) {
            double[] channelData = channel.readData();
            for (int i = 0; i < result.length; i++) {
                result[i] += channelData[i] * channel.getVolume() * volumeEnvelopeData[i];
            }
        }
        return result;
    }

    public boolean isSyncOscillator2to1() {
        return syncOscillator2to1;
    }

    public void setSyncOscillator2to1(boolean syncOscillator2to1) {
        this.syncOscillator2to1 = syncOscillator2to1;
    }
}
