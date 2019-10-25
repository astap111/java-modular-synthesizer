package com.synthesizer.channel;

public interface Channel {
    double[] readData();

    double getVolume();

    void setVolume(double volume);

    void setFrequency(double frequency);

    double getFrequency();
}
