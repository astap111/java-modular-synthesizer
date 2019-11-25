package com.synthesizer;

import com.synthesizer.channel.Channel;
import com.synthesizer.channel.generator.*;
import com.synthesizer.channel.processor.*;
import com.synthesizer.form.Key;
import com.synthesizer.form.KeyboardPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class SynthWindow extends JFrame implements EventListener {
    //Volume
    private double sineVolume = 0;
    private double triangleVolume = 0;
    private double squareVolume = 0;
    private double sawtoothVolume = 1;
    private double noiseVolume = 0;
    //ADSR
    private double attack = 3;
    private double decay = 60;
    private double sustain = 0.6;
    private double release = 4;
    //delay
    private double delay = 0.3;
    private double delayDecay = 0.5;
    private double dryWetFactor = 0.2;
    //LowPassFilter
    private BiQuadraticFilter.FilterType lpfType = BiQuadraticFilter.FilterType.LOWPASS;
    private double lpfCutoffFreq = 800;
    private double lpfQ = 0.7;
    private double lpfGain = 1;

    private int octave = 0;

    private JSlider volumeSineSlider;
    private JSlider volumeTriangleSlider;
    private JSlider volumeSquareSlider;
    private JSlider volumeSawtoothSlider;
    private JSlider noiseSlider;
    private JSlider delaySlider;
    private JSlider delayDecaySlider;
    private JSlider delayDryWetFactorSlider;
    private JSlider lpfCutOffFrequencySlider;
    private JSlider lpfSineResonanceSlider;
    private JCheckBox lpfEnableCheckbox;

    private JPanel sineWavePanel = new JPanel();
    private JPanel trialgleWavePanel = new JPanel();
    private JPanel squareWavePanel = new JPanel();
    private JPanel sawtoothWavePanel = new JPanel();
    private JPanel noisePanel = new JPanel();
    private JPanel delayPanel = new JPanel();
    private JPanel lpfPanel = new JPanel();
    private KeyboardPanel keyboardPanel = new KeyboardPanel();
    private JPanel octavePanel = new JPanel();

    private DefaultXYDataset chartDataset = new DefaultXYDataset();
    private AudioByteConverter byteConverter;
    private SineWave sineWave = new SineWave(sineVolume);
    private TriangleWave triangleWave = new TriangleWave(triangleVolume);
    private SquareWave squareWave = new SquareWave(squareVolume);
    private SawtoothWave sawtoothWave = new SawtoothWave(sawtoothVolume);
    private Noise noise = new Noise(noiseVolume);
    private Mixer mixerChannel = new Mixer(sineWave, triangleWave, squareWave, sawtoothWave, noise);
    private ADSREnvelope adsrEnvelope = new ADSREnvelope(mixerChannel, attack, decay, sustain, release);
    private Equalizer lpfChannel = new Equalizer(adsrEnvelope, lpfType, lpfCutoffFreq, lpfQ, lpfGain);
    private Delay delayChannel = new Delay(lpfChannel, delay, delayDecay, dryWetFactor);
    private Limiter mixerLimiter = new Limiter(delayChannel);
    private Channel rootChannel = mixerLimiter;

    private volatile double currentFrequency;


    public SynthWindow(AudioByteConverter byteConverter) {
        super();
        this.byteConverter = byteConverter;
        this.byteConverter.addChangeListener(this);
        JFrame.setDefaultLookAndFeelDecorated(true);
        getContentPane().setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Simple Synth");

        sineWavePanel.setBorder(BorderFactory.createTitledBorder("Sine"));
        trialgleWavePanel.setBorder(BorderFactory.createTitledBorder("Triangle"));
        squareWavePanel.setBorder(BorderFactory.createTitledBorder("Square"));
        sawtoothWavePanel.setBorder(BorderFactory.createTitledBorder("Sawtooth"));
        noisePanel.setBorder(BorderFactory.createTitledBorder("Noise"));

        BoxLayout layout1 = new BoxLayout(sineWavePanel, BoxLayout.Y_AXIS);
        BoxLayout layout2 = new BoxLayout(trialgleWavePanel, BoxLayout.Y_AXIS);
        BoxLayout layout3 = new BoxLayout(squareWavePanel, BoxLayout.Y_AXIS);
        BoxLayout layout4 = new BoxLayout(sawtoothWavePanel, BoxLayout.Y_AXIS);
        BoxLayout layout5 = new BoxLayout(noisePanel, BoxLayout.Y_AXIS);

        sineWavePanel.setLayout(layout1);
        trialgleWavePanel.setLayout(layout2);
        squareWavePanel.setLayout(layout3);
        sawtoothWavePanel.setLayout(layout4);
        noisePanel.setLayout(layout5);

        JFreeChart chart = ChartFactory.createXYLineChart("Mixer Graph", null, null, chartDataset, PlotOrientation.VERTICAL, false, false, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.getRangeAxis().setRange(-1.0, 1.0);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.getRangeAxis();

        ChartPanel chartPanel = new ChartPanel(chart);

        volumeSineSlider = createVolumeSlider(sineWave);
        sineWavePanel.add(volumeSineSlider);

        volumeTriangleSlider = createVolumeSlider(triangleWave);
        trialgleWavePanel.add(volumeTriangleSlider);

        volumeSquareSlider = createVolumeSlider(squareWave);
        squareWavePanel.add(volumeSquareSlider);

        volumeSawtoothSlider = createVolumeSlider(sawtoothWave);
        sawtoothWavePanel.add(volumeSawtoothSlider);

        noiseSlider = createVolumeSlider(noise);
        noisePanel.add(noiseSlider);

        configureDelayPanel();
        configureLowPassFilterPanel();
        configureKeyboardPanel();

        getContentPane().add(sineWavePanel);
        getContentPane().add(trialgleWavePanel);
        getContentPane().add(squareWavePanel);
        getContentPane().add(sawtoothWavePanel);
        getContentPane().add(noisePanel);
        getContentPane().add(delayPanel);
        getContentPane().add(lpfPanel);
        getContentPane().add(chartPanel);
        JPanel keyboard = new JPanel();
        keyboard.add(octavePanel);
        keyboard.add(keyboardPanel);
        getContentPane().add(keyboard);
        pack();
        setSize(1000, 900);
        setVisible(true);
        addKeyboardShortcutListener();

        rootChannel.setFrequency(440);
        byteConverter.addChannel(rootChannel);
    }

    private void configureKeyboardPanel() {
        for (Key key : keyboardPanel.getKeys()) {
            key.addChangeListener(e1 -> createKeyListener(key));
        }

        BoxLayout octavePanelLayout = new BoxLayout(octavePanel, BoxLayout.Y_AXIS);
        octavePanel.setLayout(octavePanelLayout);
        octavePanel.setAlignmentX(0.5f);
        JLabel octaveLabel = new JLabel("0");
        JButton octavePlus = new JButton("+");
        octavePlus.addActionListener(e1 -> {
            octave++;
            keyboardPanel.setOctave(octave);
            octaveLabel.setText(String.valueOf(octave));
        });
        JButton octaveMinus = new JButton("-");
        octaveMinus.addActionListener(e1 -> {
            octave--;
            keyboardPanel.setOctave(octave);
            octaveLabel.setText(String.valueOf(octave));
        });
        octavePanel.add(octavePlus);
        octavePanel.add(octaveLabel);
        octavePanel.add(octaveMinus);
    }

    private void configureDelayPanel() {
        delayPanel.setBorder(BorderFactory.createTitledBorder("Delay"));

        delaySlider = new JSlider(JSlider.HORIZONTAL, 1, 100, (int) (delay * 100));
        delaySlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            delayChannel.setDelay((double) source.getValue() / 100);
        });
        delayPanel.add(delaySlider);

        delayDecaySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (delayDecay * 100));
        delayDecaySlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            delayChannel.setDecay((double) source.getValue() / 100);
        });
        delayPanel.add(delayDecaySlider);

        delayDryWetFactorSlider = new JSlider(JSlider.HORIZONTAL, 0, 99, (int) (dryWetFactor * 100));
        delayDryWetFactorSlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            delayChannel.setDryWetFactor((double) source.getValue() / 100);
        });
        delayPanel.add(delayDryWetFactorSlider);
    }

    private void configureLowPassFilterPanel() {
        lpfPanel.setBorder(BorderFactory.createTitledBorder("Low-Pass Filter"));

        lpfEnableCheckbox = new JCheckBox();
        lpfEnableCheckbox.addChangeListener(e1 -> {
            JCheckBox source = (JCheckBox) e1.getSource();
            lpfChannel.setEnabled(source.isSelected());
        });
        lpfPanel.add(lpfEnableCheckbox);

        lpfCutOffFrequencySlider = new JSlider(JSlider.HORIZONTAL, 100, 10000, (int) (lpfCutoffFreq));
        lpfCutOffFrequencySlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            lpfChannel.setCutOffFrequency((double) source.getValue());
        });
        lpfPanel.add(lpfCutOffFrequencySlider);

        lpfSineResonanceSlider = new JSlider(JSlider.HORIZONTAL, 70, 500, (int) (lpfQ * 100));
        lpfSineResonanceSlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            lpfChannel.setResonance((double) source.getValue() / 100);
        });

        lpfPanel.add(lpfSineResonanceSlider);
    }

    private void updateChartDataset() {
        double[] yAxis = byteConverter.getLastData();
        double[] xAxis = new double[yAxis.length];
        for (int i = 0; i < xAxis.length; i++) {
            xAxis[i] = i;
        }
        double[][] data = {xAxis, yAxis};
        SwingUtilities.invokeLater(() -> {
            chartDataset.addSeries(0, data);
        });
    }

    private void createKeyListener(Key key) {
        ButtonModel model = key.getModel();
        //logModel(key);
        if (model.isArmed() && model.isPressed()) {
            if (currentFrequency == 0) {
                rootChannel.attack();
            }
            rootChannel.setFrequency(key.getNoteFrequency());
            currentFrequency = key.getNoteFrequency();
        } else {
            if (currentFrequency == 0 || currentFrequency == key.getNoteFrequency()) {
                rootChannel.release();
                currentFrequency = 0;
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
        updateChartDataset();
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
