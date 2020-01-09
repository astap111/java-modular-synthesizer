package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;

import static com.synthesizer.swing.SimpleSynth.SAMPLES;

public class Compressor implements Channel {
    private Channel channel;
    private double sensitivity;
    private double radius;

    public Compressor(Channel channel) {
        this.channel = channel;
        this.sensitivity = Math.sqrt(2);
        this.radius = 1;
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
            if (channelData[i] > 1) {
                result[i] = 1;
            } else if (channelData[i] < -1) {
                result[i] = -1;
            } else if (channelData[i] > sensitivity / 2) {
                result[i] = Math.sqrt(radius * radius - Math.pow(channelData[i] - sensitivity, 2));
            } else if (channelData[i] < -sensitivity / 2) {
                result[i] = -Math.sqrt(radius * radius - Math.pow(sensitivity + channelData[i], 2));
            } else {
                result[i] = channelData[i];
            }
        }
        return result;
    }

    @Override
    public double getVolume() {
        return 1;
    }

    @Override
    public void setVolume(double volume) {
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

    @Override
    public void setStep(double step) {
        this.channel.setStep(step);
    }

    @Override
    public double getStep() {
        return this.channel.getStep();
    }
}
