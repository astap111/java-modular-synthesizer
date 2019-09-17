package com.synthesizer;

import com.synthesizer.channel.Generator;
import com.synthesizer.channel.SynWave;
import com.synthesizer.channel.TriangleWave;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimpleSynth {
    public static int SAMPLE_RATE = 16 * 1024;
    public static int SAMPLE_LENGTH = 1;
    public static int SAMPLES = (SAMPLE_LENGTH * SAMPLE_RATE) / 1000;
    private static AudioFormat audioFormat;
    private static int SAMPLE_SIZE_IN_BITS = 16;
    private static int CHANNELS = 1;
    private static boolean SIGNED = true;
    private static boolean BIG_ENDIAN = true;
    private static SourceDataLine line;

    public static void main(String[] args) throws LineUnavailableException {
        SynthWindow synthWindow = new SynthWindow();

        Mixer mixer = new Mixer();

        Generator synWave = new TriangleWave(1.0);
        synthWindow.addGenerator(synWave);
        mixer.addChannel(synWave);

        audioFormat = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);
        line = AudioSystem.getSourceDataLine(audioFormat);
        line.open(audioFormat, 1024);
        line.start();

        synthWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                line.drain();
                line.close();
            }
        });

        int i = 0;
        while (true) {
            byte[] mixBuffer = mixer.mix(i++);
            line.write(mixBuffer, 0, mixBuffer.length);
        }
    }
}
