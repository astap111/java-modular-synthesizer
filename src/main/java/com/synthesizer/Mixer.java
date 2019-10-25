package com.synthesizer;

import com.synthesizer.channel.Channel;

import java.util.ArrayList;
import java.util.List;

import static com.synthesizer.SimpleSynth.SAMPLES;

public class Mixer {
    private volatile double[] output;
    private List<Channel> channels = new ArrayList<>();
    private EventListener listener;

    public void addChannel(Channel channel) {
        channels.add(channel);
    }

    byte[] mix() {
        double[] buffer = new double[SAMPLES];
        byte[] outputByteBuffer = new byte[SAMPLES * 2];
        int bufferSize = 0;
        for (Channel channel : channels) {
            double[] channelData = channel.readData();
            for (int i = 0; i < SAMPLES; i++) {
                buffer[i] += channelData[i] * channel.getVolume();
            }
        }

        for (int i = 0; i < SAMPLES; i++) {
            //limiter
            if (buffer[i] > 1) {
                buffer[i] = 1;
            } else if (buffer[i] < -1) {
                buffer[i] = -1;
            }
            short s = (short) (Short.MAX_VALUE * buffer[i]);
            outputByteBuffer[bufferSize++] = (byte) s;
            outputByteBuffer[bufferSize++] = (byte) (s >> 8);   // little Endian
        }

        this.output = buffer;
        listener.fireEvent();
        return outputByteBuffer;
    }

    public double[] getLastData() {
        return output;
    }

    public void addChangeListener(EventListener listener) {
        this.listener = listener;
    }
}
