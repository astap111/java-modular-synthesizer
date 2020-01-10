package com.synthesizer.javafx.form;

import javafx.scene.input.KeyCode;

import static javafx.scene.input.KeyCode.*;

public class BlackKey extends Key {
    public static final int BLACK_KEYS[] = {-8, -6, -3, -1, 1, 4, 6, 9, 11, 13, 16, 18, 21, 23, 25, 28, 30};
    private static final KeyCode keysBinded[] = {S, D, G, H, J, L, SEMICOLON, DIGIT2, DIGIT3, DIGIT4, DIGIT6, DIGIT7, DIGIT9, DIGIT0, MINUS, BACK_SPACE, null};

    public BlackKey(int pos) {
        noteFrequency = aNoteFrequency * Math.pow(noteMultiplier, BLACK_KEYS[pos]);
        keyBinded = keysBinded[pos];

        int left = 5 + WD
                + ((WD * 3) / 2) * (pos + (pos / 5)
                + ((pos + 3) / 5));
        setStyle("-fx-background-color: BLACK; -fx-background-radius: 3;");
        setLayoutX(left);
        setLayoutY(2);
        setPrefWidth(WD);
        setPrefHeight(HT * 2 / 3);
    }
}
