package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;

import java.util.Arrays;
import java.util.List;

import static com.synthesizer.SimpleSynth.SAMPLES;

public class Mixer implements Channel {
    private List<Channel> channels;

    public Mixer(Channel... channels) {
        this.channels = Arrays.asList(channels);
    }

    @Override
    public void addChannel(Channel channel) {
        this.channels.add(channel);
    }

    @Override
    public double[] readData() {
        double[] result = new double[SAMPLES];
        for (Channel channel : channels) {
            double[] channelData = channel.readData();
            for (int i = 0; i < result.length; i++) {
                result[i] += channelData[i] * channel.getVolume();
            }
        }
        return result;
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
