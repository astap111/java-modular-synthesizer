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
    private Generator cutoffEnvelope;
    private double cutoffEnvelopeDepth;

    public Equalizer(Channel channel, BiQuadraticFilter.FilterType filterType, double cutOffFrequency, double resonance, double gain) {
        this.cutOffFrequency = cutOffFrequency;
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
        double[] cutoffEnvelopeData = cutoffEnvelope.readData();

        for (int i = 0; i < channelData.length; i++) {
            if (enabled) {
                if (cutoffEnvelope != null) {
                    this.biQuadraticFilter.reconfigure(this.cutOffFrequency + cutoffEnvelopeDepth * cutoffEnvelopeData[i]);
                }
                result[i] = biQuadraticFilter.filter(channelData[i]);
            } else {
                result[i] = channelData[i];
            }
        }
        return result;
    }

    public void addCutoffEnvelope(Generator cutoffEnvelope) {
        this.cutoffEnvelope = cutoffEnvelope;
    }

    public void setCutoffEnvelopeDepth(double cutoffEnvelopeDepth) {
        this.cutoffEnvelopeDepth = cutoffEnvelopeDepth;
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
        this.biQuadraticFilter.setQ(resonance);
        this.biQuadraticFilter.reconfigure(this.cutOffFrequency);
    }
}
