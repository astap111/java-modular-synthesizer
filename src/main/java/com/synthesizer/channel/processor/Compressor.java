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

    public static void main(String[] args) {
        double sqrt2 = Math.sqrt(2);
        double sqrt5 = Math.sqrt(5);
        double b = (0.1 * sqrt2 * sqrt5 - sqrt5 + sqrt2) / (sqrt2 - sqrt5);
        System.out.println("b=" + b);
        double a = (b - 1 + sqrt5 * 0.9) / sqrt5;
        System.out.println("a=" + a);
        double r = 1 - b;
        System.out.println("r=" + r);
        a = 0.8 + 1.0 / 3;
        b = 2.0 / 3;
        r = 1.0 / 3;

        System.out.println(func(0.9, a, b));
        System.out.println(func(0.95, a, b));
        System.out.println(func(1, a, b));

    }

    static double func(double x, double a, double b) {
        double y = Math.sqrt(Math.pow(1 - b, 2) - Math.pow(x - a, 2)) + b;
        return y;
    }
}
