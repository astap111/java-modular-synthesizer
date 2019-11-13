package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;

import static com.synthesizer.SimpleSynth.SAMPLE_RATE;

public class Delay implements Channel {
    private Channel channel;
    private double delay;
    private double decay;
    private double dryWetFactor;
    private int delayInSamples;
    private double[] delayBuffer;
    private int bufferPosition = 0;

    public Delay(Channel channel, double delay, double decay, double dryWetFactor) {
        this.channel = channel;
        setDelay(delay);
        this.decay = decay;
        this.dryWetFactor = dryWetFactor;
        delayBuffer = new double[delayInSamples];
    }

    @Override
    public void addChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public double[] readData() {
        double[] data = channel.readData();

        for (int i = 0; i < data.length; i++) {
            data[i] += delayBuffer[bufferPosition] * decay;
            delayBuffer[bufferPosition] = data[i] * dryWetFactor;
            bufferPosition++;
            if (bufferPosition >= delayBuffer.length) {
                bufferPosition = 0;
            }
        }

        return data;
    }

    @Override
    public double getVolume() {
        return channel.getVolume();
    }

    @Override
    public void setVolume(double volume) {
        channel.setVolume(volume);
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

    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
        delayInSamples = (int) (SAMPLE_RATE * delay);
    }

    public double getDecay() {
        return decay;
    }

    public void setDecay(double decay) {
        this.decay = decay;
    }
}
