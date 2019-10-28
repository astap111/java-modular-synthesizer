package com.synthesizer;

import com.synthesizer.channel.Channel;
import com.synthesizer.channel.generator.*;
import com.synthesizer.channel.processor.ADSREnvelope;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SynthWindow extends JFrame implements EventListener {
    private double attack = 3;
    private double decay = 6;
    private double sustain = 0.6;
    private double release = 3;

    private JSlider volumeSine;
    private JSlider volumeTriangle;
    private JSlider volumeSquare;
    private JSlider volumeSawtooth;

    private JPanel sineWavePanel = new JPanel();
    private JPanel trialgleWavePanel = new JPanel();
    private JPanel squareWavePanel = new JPanel();
    private JPanel sawtoothWavePanel = new JPanel();
    private KeyboardPanel keyboardPanel = new KeyboardPanel();

    private DefaultXYDataset chartDataset = new DefaultXYDataset();
    private Mixer mixer;
    private List<Channel> channels = new ArrayList<>();
    private SineWave sineWave = new SineWave(1);
    private TriangleWave triangleWave = new TriangleWave(0);
    private SquareWave squareWave = new SquareWave(0);
    private SawtoothWave sawtoothWave = new SawtoothWave(0);
    private ADSREnvelope sineAdsrEnvelope = new ADSREnvelope(sineWave, attack, decay, sustain, release);
    private ADSREnvelope triangleAdsrEnvelope = new ADSREnvelope(triangleWave, attack, decay, sustain, release);
    private ADSREnvelope squareAdsrEnvelope = new ADSREnvelope(squareWave, attack, decay, sustain, release);
    private ADSREnvelope sawtoothAdsrEnvelope = new ADSREnvelope(sawtoothWave, attack, decay, sustain, release);

    private volatile double currentFrequency;


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

        volumeSine = createVolumeSlider(sineAdsrEnvelope);
        sineWavePanel.add(volumeSine);

        volumeTriangle = createVolumeSlider(triangleAdsrEnvelope);
        trialgleWavePanel.add(volumeTriangle);

        volumeSquare = createVolumeSlider(squareAdsrEnvelope);
        squareWavePanel.add(volumeSquare);

        volumeSawtooth = createVolumeSlider(sawtoothAdsrEnvelope);
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

        addChannels(sineAdsrEnvelope, triangleAdsrEnvelope, squareAdsrEnvelope, sawtoothAdsrEnvelope);
    }

    private void addChannels(Channel... chans) {
        channels.addAll(Arrays.asList(chans));
        for (Channel c : chans) {
            c.setFrequency(440);
            mixer.addChannel(c);
        }
    }

    private void updateChartDataset() {
        if (channels.isEmpty()) {
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
        //logModel(key);
        updateChartDataset();
        if (model.isArmed() && model.isPressed()) {
            for (Channel channel : channels) {
                if (currentFrequency == 0) {
                    channel.attack();
                }
                channel.setFrequency(key.getNoteFrequency());
            }
            currentFrequency = key.getNoteFrequency();
        } else {
            updateChartDataset();
            for (Channel channel : channels) {
                if (currentFrequency == 0 || currentFrequency == key.getNoteFrequency()) {
                    channel.release();
                    currentFrequency = 0;
                }
            }
        }
    }

    private void logModel(Key key) {
        ButtonModel model = key.getModel();
        System.out.println(key.getNoteFrequency());
        System.out.println("isArmed:" + model.isArmed());
        System.out.println("isPressed:" + model.isPressed());
        System.out.println("isRollover:" + model.isRollover());
        System.out.println("isSelected:" + model.isSelected());
        System.out.println("isEnabled:" + model.isEnabled());
        System.out.println("--------");
    }

    private JSlider createVolumeSlider(Channel c) {
        JSlider volumeSquare = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (c.getVolume() * 100));
        volumeSquare.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            c.setVolume((double) source.getValue() / 100);
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
