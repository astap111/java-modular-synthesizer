package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;
import com.synthesizer.javafx.util.FeedbackFeedforwardFilter;

import static com.synthesizer.javafx.util.AudioConstants.SAMPLES;


public class AllPassFilter implements Channel {
    private Channel channel;
    private FeedbackFeedforwardFilter filter;

    public AllPassFilter(Channel channel, double... coefficients) {
        this.channel = channel;
        this.filter = new FeedbackFeedforwardFilter(coefficients);
    }

    public AllPassFilter(Channel channel) {
        this.channel = channel;
        double[] coefficients = new double[1000];
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = 0.999 - i * 0.001;
        }
        this.filter = new FeedbackFeedforwardFilter(coefficients);
//        this.filter = new FeedbackFeedforwardFilter(0.99);
//        this.filter = new FeedbackFeedforwardFilter(0.999, 0.998, 0.997, 0.996, 0.995, 0.994, 0.993, 0.992, 0.991, 0.99, 0.989, 0.988, 0.987, 0.986, 0.985, 0.984, 0.983, 0.982, 0.981, 0.98, 0.979, 0.978, 0.977, 0.976, 0.975, 0.974, 0.973, 0.972, 0.971, 0.97);

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
            result[i] = filter.result(channelData[i]);
        }
        return result;
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
