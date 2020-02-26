package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;

import static com.synthesizer.swing.SimpleSynth.SAMPLES;

public class Limiter implements Channel {
    private Channel channel;
    private double volume;

    public Limiter(Channel channel) {
        this.channel = channel;
        this.volume = 1;
    }

    @Override
    public void addChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public double[] readData() {
        double[] result = new double[SAMPLES];
        double[] channelData = channel.readData();
        for (int i = 0; i < channelData.length; i++) {
            if (channelData[i] > 1) {
                result[i] = volume;
            } else if (channelData[i] < -1) {
                result[i] = -volume;
            } else {
                result[i] = channelData[i] * volume;
            }
        }
        return result;
    }

    public double getVolume() {
        return this.volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
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
}
