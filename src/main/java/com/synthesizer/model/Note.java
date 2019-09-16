package com.synthesizer.model;

public enum Note {
    Ab(415.30), A(440.00), Bb(466.16), B(493.88), C(523.25), Db(554.37), D(587.33), Eb(622.25), E(659.25), F(698.46), Gb(739.99), G(783.99);

    Note(Double frequency) {
        this.frequency = frequency;
    }

    private final Double frequency;

    public Double getFrequency() {
        return frequency;
    }
}
