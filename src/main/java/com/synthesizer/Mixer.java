package com.synthesizer;

import com.synthesizer.channel.Channel;

import java.util.ArrayList;
import java.util.List;

import static com.synthesizer.SimpleSynth.SAMPLES;

public class Mixer {
    private volatile double[] output;
    private List<Channel> channels = new ArrayList<>();

    public void addChannel(Channel channel) {
        channels.add(channel);
    }

    byte[] mix(int bufferNumber) {
        double[] buffer = new double[SAMPLES];
        byte[] outputByteBuffer = new byte[SAMPLES];
        for (Channel channel : channels) {
            double[] channelData = channel.readData(bufferNumber);
            for (int i = 0; i < SAMPLES; i++) {
                buffer[i] += channelData[i] * channel.getVolume();
            }
        }

        for (int i = 0; i < SAMPLES; i++) {
            outputByteBuffer[i] = (byte) (buffer[i] * 127f);
        }

        this.output = buffer;
        return outputByteBuffer;
    }

    public double[] getLastData() {
        return output;
    }
}
