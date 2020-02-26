package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;

import static com.synthesizer.swing.SimpleSynth.SAMPLES;

public class Compressor implements Channel {
    private Channel channel;
    private double sensitivity;
    private double radius;
    private double a;
    private double b;

    public Compressor(Channel channel) {
        this.channel = channel;
        this.sensitivity = 0.9;
        this.radius = 1d / 3;
        this.a = 0.8 + 1d / 3;
        this.b = 2d / 3;
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
            if (channelData[i] > a) {
                result[i] = 1;
            } else if (channelData[i] < -a) {
                result[i] = -1;
            } else if (channelData[i] > sensitivity) {
                result[i] = Math.sqrt(radius * radius - Math.pow(channelData[i] - a, 2)) + b;
            } else if (channelData[i] < -sensitivity) {
                result[i] = -Math.sqrt(radius * radius - Math.pow(channelData[i] + a, 2)) - b;
            } else {
                result[i] = channelData[i];
            }
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
