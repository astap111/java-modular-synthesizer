package com.synthesizer.javafx.util;

public class AudioConstants {
    public static final int SAMPLE_RATE = 44100;
    public static final int SAMPLE_LENGTH = 5;
    public static final int SAMPLES = (SAMPLE_LENGTH * SAMPLE_RATE) / 1000;
    public static final int SAMPLE_SIZE_IN_BITS = 16;
    public static final int CHANNELS = 1;
    public static final boolean SIGNED = true;
    public static final boolean BIG_ENDIAN = true;
}
