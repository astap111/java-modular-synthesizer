package com.synthesizer;

import com.synthesizer.channel.*;
import com.synthesizer.form.Key;
import com.synthesizer.form.KeyboardPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SynthWindow extends JFrame implements EventListener {
    JSlider volumeSine;
    JSlider volumeTriangle;
    JSlider volumeSquare;
    JSlider volumeSawtooth;

    JPanel sineWavePanel = new JPanel();
    JPanel trialgleWavePanel = new JPanel();
    JPanel squareWavePanel = new JPanel();
    JPanel sawtoothWavePanel = new JPanel();
    KeyboardPanel keyboardPanel = new KeyboardPanel();

    private DefaultXYDataset chartDataset = new DefaultXYDataset();
    private Mixer mixer;
    private List<Generator> generators = new ArrayList<>();
    private SineWave sineWave = new SineWave(1);
    private TriangleWave triangleWave = new TriangleWave(0);
    private SquareWave squareWave = new SquareWave(0);
    private SawtoothWave sawtoothWave = new SawtoothWave(0);


    public SynthWindow(Mixer mixer) {
        super();
        this.mixer = mixer;
        mixer.addChangeListener(this);
        JFrame.setDefaultLookAndFeelDecorated(true);
        getContentPane().setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Simple Synth");

        sineWavePanel.setBorder(BorderFactory.createTitledBorder("Sine"));
        trialgleWavePanel.setBorder(BorderFactory.createTitledBorder("Triangle"));
        squareWavePanel.setBorder(BorderFactory.createTitledBorder("Square"));
        sawtoothWavePanel.setBorder(BorderFactory.createTitledBorder("Sawtooth"));

        BoxLayout layout1 = new BoxLayout(sineWavePanel, BoxLayout.Y_AXIS);
        BoxLayout layout2 = new BoxLayout(trialgleWavePanel, BoxLayout.Y_AXIS);
        BoxLayout layout3 = new BoxLayout(squareWavePanel, BoxLayout.Y_AXIS);
        BoxLayout layout4 = new BoxLayout(sawtoothWavePanel, BoxLayout.Y_AXIS);

        sineWavePanel.setLayout(layout1);
        trialgleWavePanel.setLayout(layout2);
        squareWavePanel.setLayout(layout3);
        sawtoothWavePanel.setLayout(layout4);

        JFreeChart chart = ChartFactory.createXYLineChart("Test Chart", "x", "y", chartDataset, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        Dimension chartDimention = new Dimension();
        chartDimention.setSize(50, 50);
        chartPanel.setMaximumSize(chartDimention);
        chartPanel.setSize(chartDimention);

        volumeSine = createVolumeSlider(sineWave);
        sineWavePanel.add(volumeSine);

        volumeTriangle = createVolumeSlider(triangleWave);
        trialgleWavePanel.add(volumeTriangle);

        volumeSquare = createVolumeSlider(squareWave);
        squareWavePanel.add(volumeSquare);

        volumeSawtooth = createVolumeSlider(sawtoothWave);
        sawtoothWavePanel.add(volumeSawtooth);

        for (Key key : keyboardPanel.getKeys()) {
            key.addChangeListener(e1 -> createKeyListener(key));
        }

        getContentPane().add(sineWavePanel);
        getContentPane().add(trialgleWavePanel);
        getContentPane().add(squareWavePanel);
        getContentPane().add(sawtoothWavePanel);
        getContentPane().add(chartPanel);
        getContentPane().add(new JScrollPane(keyboardPanel));
        pack();
        setSize(1000, 900);
        setVisible(true);
        addKeyboardShortcutListener();

        addGenerators(sineWave, triangleWave, squareWave, sawtoothWave);

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
        chartDataset.addSeries("Mixer data", data);
    }

    private void createKeyListener(Key key) {
        ButtonModel model = key.getModel();
        if (model.isArmed()) {
            for (Generator generator : generators) {
                generator.startPlaying(key.getNote());
            }
        } else {
            updateChartDataset();
            for (Generator generator : generators) {
                generator.stopPlaying();
            }
        }
    }

    private JSlider createVolumeSlider(Generator g) {
        JSlider volumeSquare = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (g.getVolume() * 100));
        volumeSquare.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            g.setVolume((double) source.getValue() / 100);
        });
        return volumeSquare;
    }

    @Override
    public void fireEvent() {
//        updateChartDataset();
    }

    private void addKeyboardShortcutListener() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(keyEvent -> {
                    if (keyEvent.getID() == KeyEvent.KEY_PRESSED) {
                        Key key = keyboardPanel.getKey(keyEvent.getKeyCode());
                        if (key != null) {
                            ButtonModel model = key.getModel();
                            model.setPressed(true);
                            model.setArmed(true);
                        }
                    } else if (keyEvent.getID() == KeyEvent.KEY_RELEASED) {
                        Key key = keyboardPanel.getKey(keyEvent.getKeyCode());
                        if (key != null) {
                            ButtonModel model = key.getModel();
                            model.setPressed(false);
                            model.setArmed(false);
                        }
                    }
                    return false; //continue to monitor other key events
                });
    }
}
