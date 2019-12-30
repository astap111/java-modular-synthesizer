package com.synthesizer.javafx.form;

import javafx.scene.input.KeyCode;

import static javafx.scene.input.KeyCode.*;

public class WhiteKey extends Key {
    public static final int WHITE_KEYS[] = {-9, -7, -5, -4, -2, 0, 2, 3, 5, 7, 8, 10, 12, 14, 15, 17, 19, 20, 22, 24, 26, 27, 29, 31};
    private static final KeyCode keysBinded[] = {A, S, D, F, G, H, J, K, L, SEMICOLON, QUOTE, null, null, null, null, null, null, null, null, null, null, null, null, null};
    private int WWD = (WD * 3) / 2;

    public WhiteKey(int pos) {
        noteFrequency = aNoteFrequency * Math.pow(noteMultiplier, WHITE_KEYS[pos]);
        keyBinded = keysBinded[pos];
        int left = 5 + WWD * pos;
        setStyle("-fx-background-color: WHITE; -fx-background-radius: 3;");
        setLayoutX(left);
        setPrefWidth(WWD - 1);
        setPrefHeight(HT);
    }

}
