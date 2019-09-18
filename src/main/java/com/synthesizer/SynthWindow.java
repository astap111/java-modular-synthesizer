package com.synthesizer;

import com.synthesizer.channel.Generator;
import com.synthesizer.channel.SquareWave;
import com.synthesizer.channel.SineWave;
import com.synthesizer.channel.TriangleWave;
import com.synthesizer.model.Note;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SynthWindow extends JFrame {
    private JButton a;
    private JButton b;
    private JButton c;
    private JButton d;
    private JButton e;
    private JButton f;
    private JButton g;
    private DefaultXYDataset chartDataset;
    private Mixer mixer;
    private List<Generator> generators = new ArrayList<>();
    private SineWave sineWave = new SineWave(0.3);
    private TriangleWave triangleWave = new TriangleWave(0.3);
    private SquareWave squareWave = new SquareWave(0.3);


    public SynthWindow(Mixer mixer) {
        super();
        this.mixer = mixer;
        JFrame.setDefaultLookAndFeelDecorated(true);
        getContentPane().setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Simple Synth");

        JPanel SineWavePanel = new JPanel();
        JPanel TrialgleWavePanel = new JPanel();
        JPanel squareWavePanel = new JPanel();
        JPanel keyPanel = new JPanel();
        getContentPane().add(SineWavePanel);
        getContentPane().add(TrialgleWavePanel);
        getContentPane().add(squareWavePanel);

        SineWavePanel.setBorder(BorderFactory.createTitledBorder("Sine"));
        TrialgleWavePanel.setBorder(BorderFactory.createTitledBorder("Triangle"));
        squareWavePanel.setBorder(BorderFactory.createTitledBorder("Square"));
        keyPanel.setBorder(BorderFactory.createTitledBorder("Keys"));

        BoxLayout layout1 = new BoxLayout(SineWavePanel, BoxLayout.Y_AXIS);
        BoxLayout layout2 = new BoxLayout(TrialgleWavePanel, BoxLayout.Y_AXIS);
        BoxLayout layout3 = new BoxLayout(squareWavePanel, BoxLayout.Y_AXIS);
        SineWavePanel.setLayout(layout1);
        TrialgleWavePanel.setLayout(layout2);
        squareWavePanel.setLayout(layout3);

        a = new JButton("A");
        b = new JButton("B");
        c = new JButton("C");
        d = new JButton("D");
        e = new JButton("E");
        f = new JButton("F");
        g = new JButton("G");
        keyPanel.add(a);
        keyPanel.add(b);
        keyPanel.add(c);
        keyPanel.add(d);
        keyPanel.add(e);
        keyPanel.add(f);
        keyPanel.add(g);
        getContentPane().add(keyPanel);
//        a.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('z'), "pressed");
        a.addChangeListener(e1 -> createKeyListener(Note.A, a));
        b.addChangeListener(e1 -> createKeyListener(Note.B, b));
        c.addChangeListener(e1 -> createKeyListener(Note.C, c));
        d.addChangeListener(e1 -> createKeyListener(Note.D, d));
        e.addChangeListener(e1 -> createKeyListener(Note.E, e));
        f.addChangeListener(e1 -> createKeyListener(Note.F, f));
        g.addChangeListener(e1 -> createKeyListener(Note.G, g));

        chartDataset = new DefaultXYDataset();
        JFreeChart chart = ChartFactory.createXYLineChart("Test Chart", "x", "y", chartDataset, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setSize(100, 100);
        getContentPane().add(chartPanel);

        JSlider volumeSine = new JSlider(JSlider.HORIZONTAL, 0, 100, 30);
        SineWavePanel.add(volumeSine);
        volumeSine.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            sineWave.setVolume((double) source.getValue() / 100);
        });

        JSlider volumeTriangle = new JSlider(JSlider.HORIZONTAL, 0, 100, 30);
        TrialgleWavePanel.add(volumeTriangle);
        volumeTriangle.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            triangleWave.setVolume((double) source.getValue() / 100);
        });

        JSlider volumeSquare = new JSlider(JSlider.HORIZONTAL, 0, 100, 30);
        squareWavePanel.add(volumeSquare);
        volumeSquare.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            squareWave.setVolume((double) source.getValue() / 100);
        });

        pack();
        setSize(1000, 900);
        setVisible(true);

        addGenerators(sineWave, triangleWave, squareWave);

    }

    private void addGenerators(Generator... gens) {
        generators.addAll(Arrays.asList(gens));
        for (Generator g : gens) {
            mixer.addChannel(g);
        }
    }

    private void updateChartDataset() {
        if (generators.isEmpty()) {
            return;
        }
        double[] yAxis = mixer.getLastData();
        double[] xAxis = new double[yAxis.length];
        for (int i = 0; i < xAxis.length; i++) {
            xAxis[i] = i;
        }
        double[][] data = {xAxis, yAxis};
        chartDataset.addSeries(g.getClass().getSimpleName(), data);
    }

    private void createKeyListener(Note note, JButton key) {
        ButtonModel model = key.getModel();
        if (model.isArmed()) {
            for (Generator generator : generators) {
                generator.startPlaying(note.getFrequency());
                updateChartDataset();
            }
        } else {
            for (Generator generator : generators) {
                generator.stopPlaying();
            }
        }
    }
}
