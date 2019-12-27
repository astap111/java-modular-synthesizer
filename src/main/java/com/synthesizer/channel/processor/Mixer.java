package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;
import com.synthesizer.channel.generator.Constant;
import com.synthesizer.channel.generator.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.synthesizer.SimpleSynth.SAMPLES;

public class Mixer implements Channel {
    private List<Channel> channels = new ArrayList<>();
    private Generator volumeEnvelope = new Constant(1);

    public Mixer() {
    }

    public Mixer(Channel... channels) {
        this.channels.addAll(Arrays.asList(channels));
    }

    @Override
    public synchronized double[] readData() {
        double[] result = new double[SAMPLES];
        double[] volumeEnvelopeData = volumeEnvelope.readData();
        for (Channel channel : channels) {
            if (channel == null) {
                continue;
            }
            double[] channelData = channel.readData();
            for (int i = 0; i < result.length; i++) {
                result[i] += channelData[i] * channel.getVolume() * volumeEnvelopeData[i];
            }
        }
        return result;
    }

    @Override
    public synchronized void addChannel(Channel channel) {
        this.channels.add(channel);
        System.out.println("Added channel " + channel);
        System.out.println("Channels: " + channels);
    }

    public synchronized void removeChannel(Channel channel) {
        this.channels.remove(channel);
        System.out.println("Removed channel " + channel);
        System.out.println("Channels: " + channels);
    }

    public void addVolumeEnvelope(Generator volumeEnvelope) {
        this.volumeEnvelope = volumeEnvelope;
    }

    @Override
    public double getVolume() {
        return 1;
    }

    @Override
    public void setVolume(double volume) {
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
