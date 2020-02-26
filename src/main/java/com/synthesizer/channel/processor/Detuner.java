package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;

public class Detuner implements Channel {
    private Channel channel;
    private double detuneFactor;

    public Detuner(Channel channel) {
        this.channel = channel;
        detuneFactor = 0;
    }

    @Override
    public synchronized void addChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public double[] readData() {
        return channel.readData();
    }

    @Override
    public double getVolume() {
        return this.channel.getVolume();
    }

    @Override
    public void setVolume(double volume) {
        this.channel.setVolume(volume);
    }

    @Override
    public void setFrequency(double frequency) {
        this.channel.setFrequency(frequency * Math.pow(2, detuneFactor / 12));
    }

    @Override
    public double getFrequency() {
        return channel.getFrequency();
    }

    @Override
    public void attack() {
        this.channel.attack();
    }

    @Override
    public void release() {
        this.channel.release();
    }

    public double getDetuneFactor() {
        return detuneFactor;
    }

    public void setDetuneFactor(double detuneFactor) {
        this.detuneFactor = detuneFactor;
    }
}
