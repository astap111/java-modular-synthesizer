package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;

import java.util.Arrays;

import static com.synthesizer.swing.SimpleSynth.SAMPLES;
import static com.synthesizer.swing.SimpleSynth.SAMPLE_RATE;

public class AllPassReverb implements Channel {
    private Channel channel;
    private double delay;
    private double decay;
    private double delayAmount;
    private int delayInSamples;
    private double[] delayBuffer;
    private int bufferPosition = 0;

    public AllPassReverb(Channel channel, double delay, double decay, double delayAmount) {
        this.channel = channel;
        setDelay(delay);
        this.decay = decay;
        this.delayAmount = delayAmount;
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
            result[i] += channelData[i] - delayBuffer[bufferPosition] * delayAmount;
            delayBuffer[bufferPosition] = channelData[i] * (1 - decay * decay) + delayBuffer[bufferPosition] * decay;
            bufferPosition++;
            if (bufferPosition >= delayBuffer.length) {
                bufferPosition = 0;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        double[] channelData = new double[]{1, 0.9, 0.6, 0.2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        double[] delayBuffer = new double[4];
        double[] result = new double[32];
        int bufferPosition = 0;
        double decay = 0.7;

        for (int i = 0; i < result.length; i++) {
            result[i] += channelData[i] - delayBuffer[bufferPosition];
            delayBuffer[bufferPosition] = channelData[i] * (1 - decay * decay) + delayBuffer[bufferPosition] * decay;
            bufferPosition++;
            if (bufferPosition >= delayBuffer.length) {
                bufferPosition = 0;
            }
        }
        System.out.println(Arrays.toString(result));
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

    public double getDelayAmount() {
        return delayAmount;
    }

    public void setDelayAmount(double delayAmount) {
        this.delayAmount = delayAmount;
    }
}
