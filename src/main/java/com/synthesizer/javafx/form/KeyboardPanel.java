package com.synthesizer.javafx.form;


import javafx.scene.layout.Pane;

public class KeyboardPanel extends Pane {
    private WhiteKey[] whites = new WhiteKey[WhiteKey.WHITE_KEYS.length];
    private BlackKey[] blacks = new BlackKey[BlackKey.BLACK_KEYS.length];

    public KeyboardPanel() {
        for (int i = 0; i < whites.length; i++) {
            whites[i] = new WhiteKey(i);
            getChildren().add(whites[i]);
        }
        for (int i = 0; i < blacks.length; i++) {
            blacks[i] = new BlackKey(i);
            getChildren().add(blacks[i]);
        }
    }

    public Key[] getKeys() {
        Key[] result = new Key[blacks.length + whites.length];
        int i = 0;
        for (Key k : blacks) {
            result[i++] = k;
        }
        for (Key k : whites) {
            result[i++] = k;
        }
        return result;
    }

    public Key getKey(int keyCode) {
        for (WhiteKey white : whites) {
            if (white.getKeyBinded() == keyCode) {
                return white;
            }
        }
        for (BlackKey black : blacks) {
            if (black.getKeyBinded() == keyCode) {
                return black;
            }
        }
        return null;
    }

    public void setOctave(int i) {
        for (WhiteKey white : whites) {
            white.setOctave(i);
        }
        for (BlackKey black : blacks) {
            black.setOctave(i);
        }
    }
}
