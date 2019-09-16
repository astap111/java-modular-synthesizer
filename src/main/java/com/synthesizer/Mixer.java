package com.synthesizer;

import com.synthesizer.channel.Channel;

import java.util.ArrayList;
import java.util.List;

import static com.synthesizer.SimpleSynth.SAMPLES;

public class Mixer {
    private List<Channel> channels = new ArrayList<>();

    public void addChannel(Channel channel) {
        channels.add(channel);
    }


    byte[] mix(int bufferNumber) {
        double[] buffer = new double[SAMPLES];
        byte[] output = new byte[SAMPLES];
        for (Channel channel : channels) {
            double[] channelData = channel.getData(bufferNumber);
            for (int i = 0; i < SAMPLES; i++) {
                buffer[i] += channelData[i] * channel.getVolume();
            }
        }

        for (int i = 0; i < SAMPLES; i++) {
            output[i] = (byte) (buffer[i] * 127f);
            //            output[i] = (byte) (Math.sin(angle) * 127f);
        }

        return output;
    }
}
