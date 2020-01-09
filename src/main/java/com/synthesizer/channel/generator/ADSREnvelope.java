package com.synthesizer.channel.generator;

import static com.synthesizer.swing.SimpleSynth.SAMPLE_RATE;

public class ADSREnvelope extends Generator {
    private double attack;
    private double decay;
    private double sustain;
    private double release;
    private double curve;
    private boolean isPushed;
    private Stage stage;

    public ADSREnvelope(double attack, double decay, double sustain, double release) {
        this.attack = attack;
        this.decay = decay;
        this.sustain = sustain;
        this.release = release;
    }

    @Override
    protected void genetateWave(double[] output) {
        for (int i = 0; i < output.length; i++) {
            if (isPushed) {
                switch (stage) {
                    case ATTACK:
                        curve += 1000.0 / (SAMPLE_RATE * this.attack);
                        if (curve >= 1) {
                            stage = Stage.DECAY;
                            curve = 1;
                        }
                        break;

                    case DECAY:
                        curve -= 1000.0 / (SAMPLE_RATE * this.decay);
                        if (curve <= sustain) {
                            stage = Stage.SUSTAIN;
                            curve = sustain;
                        }
                        break;

                    case SUSTAIN:
                        curve = sustain;
                        break;
                }
            } else {
                if (curve > 0) {
                    curve -= 1000.0 / (SAMPLE_RATE * this.release);
                    if (curve <= 0) {
                        curve = 0;
                    }
                }
            }
            output[i] = curve;
        }
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getDecay() {
        return decay;
    }

    public void setDecay(double decay) {
        this.decay = decay;
    }

    public double getSustain() {
        return sustain;
    }

    public void setSustain(double sustain) {
        this.sustain = sustain;
    }

    public double getRelease() {
        return release;
    }

    public void setRelease(double release) {
        this.release = release;
    }

    @Override
    public void attack() {
        this.stage = Stage.ATTACK;
        this.isPushed = true;
    }

    @Override
    public void release() {
        this.isPushed = false;
        this.stage = Stage.RELEASE;
    }

    public enum Stage {
        ATTACK, DECAY, SUSTAIN, RELEASE;
    }
}
