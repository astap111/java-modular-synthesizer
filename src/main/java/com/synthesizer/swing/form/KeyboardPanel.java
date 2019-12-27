package com.synthesizer.swing.form;

import javax.swing.*;
import java.awt.*;

public class KeyboardPanel extends JPanel {
    private WhiteKey[] whites = new WhiteKey[10];
    private BlackKey[] blacks = new BlackKey[7];

    public KeyboardPanel() {
        setLayout(null);

        for (int i = 0; i < blacks.length; i++) {
            blacks[i] = new BlackKey(i);
            add(blacks[i]);
        }
        for (int i = 0; i < whites.length; i++) {
            whites[i] = new WhiteKey(i);
            add(whites[i]);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int count = getComponentCount();
        Component last = getComponent(count - 1);
        Rectangle bounds = last.getBounds();
        int width = 10 + bounds.x + bounds.width;
        int height = 10 + bounds.y + bounds.height;

        return new Dimension(width, height);
    }

    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
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
