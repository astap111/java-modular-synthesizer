package com.synthesizer.channel;

public interface Channel {
    double getVolume();
    void setVolume(double volume);
    double[] readData(int bufferNumber);
}
