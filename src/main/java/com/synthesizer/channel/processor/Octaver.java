package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;
import com.synthesizer.javafx.util.OctaveKnob;

public class Octaver implements Channel {
    private Channel channel;
    private double octaveFactor;

    public Octaver(Channel channel, OctaveKnob octaveKnob) {
        this.channel = channel;
        this.octaveFactor = octaveKnob.getOctaveFactor();
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
    public void setFrequency(double frequency) {
        this.channel.setFrequency(frequency / octaveFactor);
    }

    @Override
    public double getFrequency() {
        return channel.getFrequency() * octaveFactor;
    }

    @Override
    public void attack() {
        this.channel.attack();
    }

    @Override
    public void release() {
        this.channel.release();
    }

    public double getOctaveFactor() {
        return octaveFactor;
    }

    public void setOctaveFactor(double octaveFactor) {
        this.octaveFactor = octaveFactor;
    }
}
