package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;
import com.synthesizer.channel.generator.Generator;
import com.synthesizer.channel.generator.Noise;

import static com.synthesizer.swing.SimpleSynth.SAMPLES;

public class GrandmotherMixer extends Mixer {

    private volatile boolean syncOscillator2to1 = false;
    private Noise noise;
    private Generator oscillator1;
    private Generator oscillator2;

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

        double[] noiseData = noise.readData();
        double[] oscillator1Data = oscillator1.readData();
        double[] oscillator2Data = oscillator2.readData();
        for (int i = 0; i < noiseData.length; i++) {
            result[i] += noiseData[i] * noise.getVolume() * volumeEnvelopeData[i];
            result[i] += oscillator1Data[i] * oscillator1.getVolume() * volumeEnvelopeData[i];
            result[i] += oscillator2Data[i] * oscillator2.getVolume() * volumeEnvelopeData[i];
        }
        return result;
    }

    public boolean isSyncOscillator2to1() {
        return syncOscillator2to1;
    }

    public void setSyncOscillator2to1(boolean syncOscillator2to1) {
        this.syncOscillator2to1 = syncOscillator2to1;
    }

    public void setNoise(Noise noise) {
        this.noise = noise;
    }

    public void setOscillator1(Generator oscillator1) {
        this.oscillator1 = oscillator1;
    }

    public void setOscillator2(Generator oscillator2) {
        this.oscillator2 = oscillator2;
    }
}
