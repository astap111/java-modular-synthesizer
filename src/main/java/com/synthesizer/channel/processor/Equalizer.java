package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;
import com.synthesizer.channel.generator.Generator;
import com.synthesizer.javafx.util.BiQuadraticFilter;

import static com.synthesizer.swing.SimpleSynth.SAMPLES;
import static com.synthesizer.swing.SimpleSynth.SAMPLE_RATE;

public class Equalizer implements Channel {
    private Channel channel;
    private BiQuadraticFilter biQuadraticFilter;
    private boolean enabled;
    private double cutOffFrequency;
    private Generator cutoffEnvelope;
    private double cutoffEnvelopeDepth;
    private double kbdTrack;

    public Equalizer(Channel channel, BiQuadraticFilter.FilterType filterType, double cutOffFrequency, double resonance, double gain) {
        this.cutOffFrequency = cutOffFrequency;
        this.channel = channel;
        this.biQuadraticFilter = new BiQuadraticFilter(filterType, cutOffFrequency, SAMPLE_RATE, resonance, gain);
    }

    public Equalizer(Channel channel, BiQuadraticFilter.FilterType filterType, double cutOffFrequency, double resonance, double gain, double cutoffEnvelopeDepth) {
        this(channel, filterType, cutOffFrequency, resonance, gain);
        this.cutoffEnvelopeDepth = cutoffEnvelopeDepth;
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
                double envelopeDelta = 0;
                if (cutoffEnvelope != null) {
                    envelopeDelta = cutoffEnvelopeDepth * cutoffEnvelopeData[i];
                }
                double kbdTrackDelta = 0;
                if (kbdTrack != 0) {
                    kbdTrackDelta = kbdTrack * getFrequency();
                }
                biQuadraticFilter.reconfigure(cutOffFrequency + envelopeDelta + kbdTrackDelta);
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

    @Override
    public void setStep(double step) {
        this.channel.setStep(step);
    }

    @Override
    public double getStep() {
        return this.channel.getStep();
    }

    public double getKbdTrack() {
        return kbdTrack;
    }

    public void setKbdTrack(double kbdTrack) {
        this.kbdTrack = kbdTrack;
    }
}
