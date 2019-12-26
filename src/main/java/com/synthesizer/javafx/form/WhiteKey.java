package com.synthesizer.javafx.form;

public class WhiteKey extends Key {
    public static final int WHITE_KEYS[] = {-9, -7, -5, -4, -2, 0, 2, 3, 5, 7, 8, 10, 12, 14, 15, 17, 19, 20, 22, 24, 26, 27, 29, 31};
    //    private static final int keysBinded[] = {VK_Z, VK_X, VK_C, VK_V, VK_B, VK_N, VK_M, VK_COMMA, VK_PERIOD, VK_SLASH};
    private int WWD = (WD * 3) / 2;

    public WhiteKey(int pos) {
        noteFrequency = aNoteFrequency * Math.pow(noteMultiplier, WHITE_KEYS[pos]);
//        keyBinded = keysBinded[pos];
        int left = 5 + WWD * pos;
        setStyle("-fx-background-color: WHITE; -fx-background-radius: 3;");
        setLayoutX(left);
        setPrefWidth(WWD - 1);
        setPrefHeight(HT);
    }

}
