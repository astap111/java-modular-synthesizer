package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;

import static com.synthesizer.swing.SimpleSynth.SAMPLES;
import static com.synthesizer.swing.SimpleSynth.SAMPLE_RATE;

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
        double[] result = new double[SAMPLES];
        double[] channelData = channel.readData();

        for (int i = 0; i < result.length; i++) {
            result[i] += channelData[i] * (1 - dryWetFactor) + delayBuffer[bufferPosition] * decay * dryWetFactor;
            delayBuffer[bufferPosition] = channelData[i] + delayBuffer[bufferPosition] * decay;
            bufferPosition++;
            if (bufferPosition >= delayBuffer.length) {
                bufferPosition = 0;
            }
        }

        return result;
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
        delayBuffer = new double[delayInSamples];
        bufferPosition = 0;
    }

    public double getDecay() {
        return decay;
    }

    public void setDecay(double decay) {
        this.decay = decay;
    }

    public double getDryWetFactor() {
        return dryWetFactor;
    }

    public void setDryWetFactor(double dryWetFactor) {
        this.dryWetFactor = dryWetFactor;
    }
}
