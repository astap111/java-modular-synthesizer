package com.synthesizer;

import javax.sound.sampled.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimpleSynth {
    public static int SAMPLE_RATE = 44100; //samples per second
    public static int SAMPLE_LENGTH = 5; //buffer in millis
    public static int SAMPLES = (SAMPLE_LENGTH * SAMPLE_RATE) / 1000;
    private static AudioFormat audioFormat;
    private static int SAMPLE_SIZE_IN_BITS = 8;
    private static int CHANNELS = 1;
    private static boolean SIGNED = true;
    private static boolean BIG_ENDIAN = true;
    private static SourceDataLine line;

    public static void main(String[] args) throws LineUnavailableException {
        Mixer mixer = new Mixer();

        SynthWindow synthWindow = new SynthWindow(mixer);

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
