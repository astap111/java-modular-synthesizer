package com.synthesizer.channel;

public interface Channel {
    void addChannel(Channel channel);

    double[] readData();

    void setFrequency(double frequency);

    double getFrequency();

    void attack();

    void release();
}
