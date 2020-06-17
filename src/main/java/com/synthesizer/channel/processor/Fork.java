package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;

import static com.synthesizer.swing.SimpleSynth.SAMPLES;

public class Fork implements Channel {

    private Channel source;
    private Channel[] comsumers;
    double[] channelData;
    int i = 0;

    public Fork(Channel source, Channel... consumers) {
        this.source = source;
        this.comsumers = consumers;
        for (Channel consumer : consumers) {
            consumer.addChannel(this);
        }
    }

    @Override
    public void addChannel(Channel channel) {
        this.source = channel;
    }

    @Override
    public double[] readData() {
        double[] result = new double[SAMPLES];
        if (i++ % comsumers.length == 0) {
            channelData = source.readData();
        }
        System.arraycopy(channelData, 0, result, 0, result.length);
        return result;
    }

    @Override
    public void setFrequency(double frequency) {
        source.setFrequency(frequency);
    }

    @Override
    public double getFrequency() {
        return source.getFrequency();
    }

    @Override
    public void attack() {
        comsumers[0].attack();
    }

    @Override
    public void release() {
        comsumers[0].release();
    }
}
