package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;

public class JCReverb implements Channel {

    private Channel channel;
    private double dryWetFactor;
    private Fork reverbFork;
    private Delay delay1;
    private Delay delay2;
    private Delay delay3;
    private Delay delay4;
    private AllPassReverb allPassReverb1;
    private AllPassReverb allPassReverb2;
    private Mixer reverbForkMixer;

    public JCReverb(Channel channel, double dryWetFactor) {
        this.channel = channel;
        this.dryWetFactor = dryWetFactor;
        delay1 = new Delay(null, 0.170356735670, 0.7, dryWetFactor);
        delay2 = new Delay(null, 0.1452305934052, 0.7, dryWetFactor);
        delay3 = new Delay(null, 0.114455634656, 0.7, dryWetFactor);
        delay4 = new Delay(null, 0.094262625279, 0.7, dryWetFactor);
        reverbFork = new Fork(channel, delay1, delay2, delay3, delay4);
        reverbForkMixer = new Mixer(delay1, delay2, delay3, delay4);
        allPassReverb1 = new AllPassReverb(reverbForkMixer, 0.050813973567, 0.7, dryWetFactor);
        allPassReverb2 = new AllPassReverb(allPassReverb1, 0.0172356445672, 0.7, dryWetFactor);
    }

    @Override
    public void addChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public double[] readData() {
        return allPassReverb2.readData();
    }

    @Override
    public void setFrequency(double frequency) {
        channel.setFrequency(frequency);
    }

    @Override
    public double getFrequency() {
        return channel.getFrequency();
    }

    @Override
    public void attack() {
        channel.attack();
    }

    @Override
    public void release() {
        channel.release();
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public double getDryWetFactor() {
        return dryWetFactor;
    }

    public void setDryWetFactor(double dryWetFactor) {
        this.dryWetFactor = dryWetFactor;
        delay1.setDryWetFactor(dryWetFactor);
        delay2.setDryWetFactor(dryWetFactor);
        delay3.setDryWetFactor(dryWetFactor);
        delay4.setDryWetFactor(dryWetFactor);
        allPassReverb1.setDelayAmount(dryWetFactor);
        allPassReverb2.setDelayAmount(dryWetFactor);
    }
}
