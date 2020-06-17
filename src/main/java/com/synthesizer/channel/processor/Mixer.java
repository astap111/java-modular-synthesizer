package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;
import com.synthesizer.channel.generator.Constant;
import com.synthesizer.channel.generator.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.synthesizer.swing.SimpleSynth.SAMPLES;

public class Mixer implements Channel {
    protected List<Channel> channels = new ArrayList<>();
    protected Generator volumeEnvelope = new Constant(1);

    public Mixer() {
    }

    public Mixer(Channel... channels) {
        this.channels.addAll(Arrays.asList(channels));
        this.volumeEnvelope = new Constant(1.0 / channels.length);
    }

    @Override
    public double[] readData() {
        double[] result = new double[SAMPLES];
        double[] volumeEnvelopeData = volumeEnvelope.readData();

        for (Channel channel : channels) {
            double[] channelData = channel.readData();
            for (int i = 0; i < result.length; i++) {
                result[i] += channelData[i] * volumeEnvelopeData[i];
            }
        }
        return result;
    }

    @Override
    public void addChannel(Channel channel) {
        this.channels.add(channel);
    }

    public void addVolumeEnvelope(Generator volumeEnvelope) {
        this.volumeEnvelope = volumeEnvelope;
    }

    @Override
    public void setFrequency(double frequency) {
        for (Channel channel : this.channels) {
            channel.setFrequency(frequency);
        }
    }

    @Override
    public double getFrequency() {
        return this.channels.get(0).getFrequency();
    }

    @Override
    public void attack() {
        for (Channel channel : this.channels) {
            channel.attack();
        }
    }

    @Override
    public void release() {
        for (Channel channel : this.channels) {
            channel.release();
        }
    }
}
