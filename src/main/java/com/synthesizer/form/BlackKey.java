package com.synthesizer.form;

import java.awt.*;

public class BlackKey extends Key {

    private final double note;
    private static final int blackKeys[] = {-8, -6, -3, -1, 1, 4, 6};

    public BlackKey(int pos) {
        note = aNoteFrequency * Math.pow(noteMultiplier, blackKeys[pos]);

        int left = 10 + WD
                + ((WD * 3) / 2) * (pos + (pos / 5)
                + ((pos + 3) / 5));
        setBackground(Color.BLACK);
        setBounds(left, 10, WD, HT);
    }

    public double getNote() {
        return note;
    }
}
