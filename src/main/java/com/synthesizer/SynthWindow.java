package com.synthesizer;

import com.synthesizer.channel.Generator;
import com.synthesizer.model.Note;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SynthWindow extends JFrame {
    private JButton a;
    private JButton b;
    private JButton c;
    private JButton d;
    private JButton e;
    private JButton f;
    private JButton g;

    private List<Generator> generators = new ArrayList<>();

    public SynthWindow() {
        super();

        a = new JButton("A");
        b = new JButton("B");
        c = new JButton("C");
        d = new JButton("D");
        e = new JButton("E");
        f = new JButton("F");
        g = new JButton("G");
        getContentPane().add(a);
        getContentPane().add(b);
        getContentPane().add(c);
        getContentPane().add(d);
        getContentPane().add(e);
        getContentPane().add(f);
        getContentPane().add(g);

        getContentPane().setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 70);
        setVisible(true);
        setTitle("Simple Synth");

        a.addChangeListener(e1 -> createKeyListener(Note.A, a));
        b.addChangeListener(e1 -> createKeyListener(Note.B, b));
        c.addChangeListener(e1 -> createKeyListener(Note.C, c));
        d.addChangeListener(e1 -> createKeyListener(Note.D, d));
        e.addChangeListener(e1 -> createKeyListener(Note.E, e));
        f.addChangeListener(e1 -> createKeyListener(Note.F, f));
        g.addChangeListener(e1 -> createKeyListener(Note.G, g));
    }

    public void addGenerator(Generator generator) {
        this.generators.add(generator);
    }

    private void createKeyListener(Note note, JButton key) {
        ButtonModel model = key.getModel();
        if (model.isArmed()) {
            for (Generator generator : generators) {
                generator.startPlaying(note.getFrequency());
            }
        } else {
            for (Generator generator : generators) {
                generator.stopPlaying();
            }
        }
    }
}
