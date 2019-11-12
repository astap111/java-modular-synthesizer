package com.synthesizer.channel;

public interface Channel {
    void addChannel(Channel channel);

    double[] readData();

    double getVolume();

    void setVolume(double volume);

    void setFrequency(double frequency);

    double getFrequency();

    void attack();

    void release();
}
