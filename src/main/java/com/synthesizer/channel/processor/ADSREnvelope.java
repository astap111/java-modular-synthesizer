package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;

public class ADSREnvelope implements Channel {
    private Channel channel;
    private double attack;
    private double decay;
    private double sustain;
    private double release;

    public ADSREnvelope(Channel channel, double attack, double decay, double sustain, double release) {
        this.channel = channel;
        this.attack = attack;
        this.decay = decay;
        this.sustain = sustain;
        this.release = release;
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
        this.channel.setFrequency(frequency);
    }

    @Override
    public double getFrequency() {
        return this.channel.getFrequency();
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getDecay() {
        return decay;
    }

    public void setDecay(double decay) {
        this.decay = decay;
    }

    public double getSustain() {
        return sustain;
    }

    public void setSustain(double sustain) {
        this.sustain = sustain;
    }

    public double getRelease() {
        return release;
    }

    public void setRelease(double release) {
        this.release = release;
    }
}
