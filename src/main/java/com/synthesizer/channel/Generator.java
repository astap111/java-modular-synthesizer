package com.synthesizer.channel;

public interface Generator extends Channel {
    double[] getData(int bufferNumber);

    void startPlaying(double frequency);
    void stopPlaying();
}
