package com.synthesizer.channel.processor;

import com.synthesizer.channel.Channel;

import static com.synthesizer.SimpleSynth.SAMPLES;
import static com.synthesizer.SimpleSynth.SAMPLE_RATE;

public class Equalizer implements Channel {
    private Channel channel;
    private BiQuadraticFilter filter;
    private boolean enabled;

    public Equalizer(Channel channel, BiQuadraticFilter.FilterType filterType, double frequency, double q, double gain) {
        this.channel = channel;
        this.filter = new BiQuadraticFilter(filterType, frequency, SAMPLE_RATE, q, gain);
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
            if (enabled) {
                result[i] = filter.filter(channelData[i]);
            } else {
                result[i] = channelData[i];
            }
        }
        return result;
    }

    @Override
    public double getVolume() {
        return channel.getVolume();
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

    @Override
    public void attack() {
        this.channel.attack();
    }

    @Override
    public void release() {
        this.channel.release();
    }

    public void setCutOffFrequency(double value) {
        this.filter.reconfigure(value);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
