package com.synthesizer;

import com.synthesizer.channel.Channel;

import static com.synthesizer.SimpleSynth.SAMPLES;

public class AudioByteConverter {
    private double[] output;
    private Channel channel;
    private EventListener listener;

    public void addChannel(Channel channel) {
        this.channel = channel;
    }

    byte[] getByteArray() {
        byte[] outputByteBuffer = new byte[SAMPLES * 2];
        int bufferSize = 0;
        double[] buffer = channel.readData();

        for (int i = 0; i < SAMPLES; i++) {
            short s = (short) (Short.MAX_VALUE * buffer[i]);
            outputByteBuffer[bufferSize++] = (byte) (s >> 8);
            outputByteBuffer[bufferSize++] = (byte) s; //big Endian
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
