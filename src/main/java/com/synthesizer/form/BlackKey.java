package com.synthesizer.form;

import java.awt.*;

import static java.awt.event.KeyEvent.*;


public class BlackKey extends Key {

    private static final int blackKeys[] = {-8, -6, -3, -1, 1, 4, 6};
    private static final int keysBinded[] = {VK_S, VK_D, VK_G, VK_H, VK_J, VK_L, VK_SEMICOLON};

    public BlackKey(int pos) {
        noteFrequency = aNoteFrequency * Math.pow(noteMultiplier, blackKeys[pos]);
        keyBinded = keysBinded[pos];

        int left = 10 + WD
                + ((WD * 3) / 2) * (pos + (pos / 5)
                + ((pos + 3) / 5));
        setBackground(Color.BLACK);
        setBounds(left, 10, WD, HT);
    }

    public double getNoteFrequency() {
        return noteFrequency;
    }
}
