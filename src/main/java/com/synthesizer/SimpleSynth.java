package com.synthesizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimpleSynth {
    public static int SAMPLE_RATE = 44100;
    public static int SAMPLE_LENGTH = 5;
    public static int SAMPLES = (SAMPLE_LENGTH * SAMPLE_RATE) / 1000;
    private static AudioFormat audioFormat;
    private static int SAMPLE_SIZE_IN_BITS = 16;
    private static int CHANNELS = 1;
    private static boolean SIGNED = true;
    private static boolean BIG_ENDIAN = false;
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
                line.stop();
                line.close();
                line = null;
            }
        });

        Thread t = new Thread(() -> {
            while (true) {
                byte[] mixBuffer = mixer.mix();
                line.write(mixBuffer, 0, mixBuffer.length);
            }
        });
        t.run();
    }
}
