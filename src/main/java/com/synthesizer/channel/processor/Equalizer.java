package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;
import com.synthesizer.channel.generator.Generator;

import static com.synthesizer.SimpleSynth.SAMPLES;
import static com.synthesizer.SimpleSynth.SAMPLE_RATE;

public class Equalizer implements Channel {
    private Channel channel;
    private BiQuadraticFilter biQuadraticFilter;
    private boolean enabled;
    private double cutOffFrequency;
    private Generator resonanceEnvelope;
    private double resonance;

    public Equalizer(Channel channel, BiQuadraticFilter.FilterType filterType, double cutOffFrequency, double resonance, double gain) {
        this.cutOffFrequency = cutOffFrequency;
        this.resonance = resonance;
        this.channel = channel;
        this.biQuadraticFilter = new BiQuadraticFilter(filterType, cutOffFrequency, SAMPLE_RATE, resonance, gain);
    }

    @Override
    public void addChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public double[] readData() {
        double[] result = new double[SAMPLES];
        double[] channelData = channel.readData();
        double[] resonanceEnvelopeData = resonanceEnvelope.readData();

        for (int i = 0; i < channelData.length; i++) {
            if (enabled) {
                if (resonanceEnvelope != null) {
                    this.biQuadraticFilter.setQ(resonance * resonanceEnvelopeData[i]);
                    this.biQuadraticFilter.reconfigure(this.cutOffFrequency);
                }
                result[i] = biQuadraticFilter.filter(channelData[i]);
            } else {
                result[i] = channelData[i];
            }
        }
        return result;
    }

    public void addResonanceEnvelope(Generator resonanceEnvelope) {
        this.resonanceEnvelope = resonanceEnvelope;
    }

    @Override
    public double getVolume() {
        return channel.getVolume();
    }

    @Override
    public void setVolume(double volume) {
        this.channel.setVolume(volume);
    }

    @Override
    public void setFrequency(double frequency) {
        this.channel.setFrequency(frequency);
    }

    @Override
    public double getFrequency() {
        return this.channel.getFrequency();
    }

    @Override
    public void attack() {
        this.channel.attack();
    }

    @Override
    public void release() {
        this.channel.release();
    }

    public void setCutOffFrequency(double cutOffFrequency) {
        this.cutOffFrequency = cutOffFrequency;
        this.biQuadraticFilter.reconfigure(cutOffFrequency);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setResonance(double resonance) {
        this.resonance = resonance;
        this.biQuadraticFilter.setQ(resonance);
        this.biQuadraticFilter.reconfigure(this.cutOffFrequency);
    }
}
