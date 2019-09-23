package com.synthesizer.form;

import static java.awt.event.KeyEvent.*;

public class WhiteKey extends Key {

    private int WWD = (WD * 3) / 2;
    private int WHT = (HT * 3) / 2;
    private static final int whiteKeys[] = {-9, -7, -5, -4, -2, 0, 2, 3, 5, 7};
    private static final int keysBinded[] = {VK_Z, VK_X, VK_C, VK_V, VK_B, VK_N, VK_M, VK_COMMA, VK_PERIOD, VK_SLASH};

    public WhiteKey(int pos) {
        note = aNoteFrequency * Math.pow(noteMultiplier, whiteKeys[pos]);
        keyBinded = keysBinded[pos];

        int left = 10 + WWD * pos;
        // I think metal looks better!
        //setBackground (Color.WHITE);
        setBounds(left, 10, WWD, WHT);
    }

}
