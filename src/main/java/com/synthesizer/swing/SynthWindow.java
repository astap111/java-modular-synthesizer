package com.synthesizer.swing;

import com.synthesizer.javafx.util.AudioByteConverter;
import com.synthesizer.javafx.util.EventListener;
import com.synthesizer.channel.Channel;
import com.synthesizer.channel.generator.*;
import com.synthesizer.channel.processor.*;
import com.synthesizer.swing.form.Key;
import com.synthesizer.swing.form.KeyboardPanel;
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
    private double attack = 1;
    private double decay = 600;
    private double sustain = 0.6;
    private double release = 600;
    //delay
    private double delay = 0.3;
    private double delayDecay = 0.5;
    private double dryWetFactor = 0;
    //LowPassFilter
    private BiQuadraticFilter.FilterType lpfType = BiQuadraticFilter.FilterType.LOWPASS;
    private boolean lpfEnabled = true;
    private double lpfCutoffFreq = 300;
    private double lpfQ = 2;
    private double lpfGain = 20;
    private double lpfCutoffEnvelopeDepth = 700;

    private int octave = -2;

    private JSlider volumeSineSlider;
    private JSlider volumeTriangleSlider;
    private JSlider volumeSquareSlider;
    private JSlider volumeSawtoothSlider;
    private JSlider noiseSlider;
    private JSlider attackSlider;
    private JSlider decaySlider;
    private JSlider sustainSlider;
    private JSlider releaseSlider;
    private JSlider delaySlider;
    private JSlider delayDecaySlider;
    private JSlider delayDryWetFactorSlider;
    private JSlider lpfCutOffFrequencySlider;
    private JSlider lpfResonanceSlider;
    private JSlider lpfCutoffEnvelopeDepthSlider;
    private JCheckBox lpfEnableCheckbox;

    private JPanel oscillatorsPanel = new JPanel();
    private JPanel adsrPanel = new JPanel();
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
    private ADSREnvelope volumeAdsrEnvelope = new ADSREnvelope(attack, decay, sustain, release);
    private Equalizer lpfChannel = new Equalizer(mixerChannel, lpfType, lpfCutoffFreq, lpfQ, lpfGain);
    private Delay delayChannel = new Delay(lpfChannel, delay, delayDecay, dryWetFactor);
    private Limiter mixerLimiter = new Limiter(delayChannel);
    private Channel rootChannel = mixerLimiter;

    private volatile double currentFrequency;


    public SynthWindow(AudioByteConverter byteConverter) {
        super();

        mixerChannel.addVolumeEnvelope(volumeAdsrEnvelope);
        lpfChannel.addCutoffEnvelope(volumeAdsrEnvelope);
        lpfChannel.setCutoffEnvelopeDepth(lpfCutoffEnvelopeDepth);
        lpfChannel.setEnabled(lpfEnabled);
        this.byteConverter = byteConverter;
        this.byteConverter.addChangeListener(this);
        JFrame.setDefaultLookAndFeelDecorated(true);
        getContentPane().setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Simple Synth");


        JFreeChart chart = ChartFactory.createXYLineChart("Mixer Graph", null, null, chartDataset, PlotOrientation.VERTICAL, false, false, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.getRangeAxis().setRange(-1.0, 1.0);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.getRangeAxis();

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(300, 200));

        configureOscillatorsPanel();
        configureAdsrPanel();
        configureDelayPanel();
        configureLowPassFilterPanel();
        configureKeyboardPanel();

        getContentPane().add(oscillatorsPanel);
        getContentPane().add(adsrPanel);
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

    private void configureOscillatorsPanel() {
        oscillatorsPanel.setBorder(BorderFactory.createTitledBorder("Oscillators"));

        volumeSineSlider = createVolumeSlider(sineWave);
        oscillatorsPanel.add(volumeSineSlider);

        volumeTriangleSlider = createVolumeSlider(triangleWave);
        oscillatorsPanel.add(volumeTriangleSlider);

        volumeSquareSlider = createVolumeSlider(squareWave);
        oscillatorsPanel.add(volumeSquareSlider);

        volumeSawtoothSlider = createVolumeSlider(sawtoothWave);
        oscillatorsPanel.add(volumeSawtoothSlider);

        noiseSlider = createVolumeSlider(noise);
        oscillatorsPanel.add(noiseSlider);
    }

    private void configureAdsrPanel() {
        adsrPanel.setBorder(BorderFactory.createTitledBorder("ADSR"));

        attackSlider = new JSlider(JSlider.VERTICAL, 0, 1000, (int) attack);
        attackSlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            volumeAdsrEnvelope.setAttack(source.getValue());
        });
        adsrPanel.add(attackSlider);

        releaseSlider = new JSlider(JSlider.VERTICAL, 0, 1000, (int) release);
        releaseSlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            volumeAdsrEnvelope.setRelease(source.getValue());
        });
        adsrPanel.add(releaseSlider);

        sustainSlider = new JSlider(JSlider.VERTICAL, 0, 100, (int) (sustain * 100));
        sustainSlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            volumeAdsrEnvelope.setSustain((double) source.getValue() / 100);
        });
        adsrPanel.add(sustainSlider);


        decaySlider = new JSlider(JSlider.VERTICAL, 0, 1000, (int) decay);
        decaySlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            volumeAdsrEnvelope.setDecay(source.getValue());
        });
        adsrPanel.add(decaySlider);
    }

    private void configureKeyboardPanel() {
        for (Key key : keyboardPanel.getKeys()) {
            key.addChangeListener(e1 -> createKeyListener(key));
        }

        BoxLayout octavePanelLayout = new BoxLayout(octavePanel, BoxLayout.Y_AXIS);
        octavePanel.setLayout(octavePanelLayout);
        octavePanel.setAlignmentX(0.5f);
        JLabel octaveLabel = new JLabel(String.valueOf(octave));
        keyboardPanel.setOctave(octave);
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

        delaySlider = new JSlider(JSlider.VERTICAL, 1, 100, (int) (delay * 100));
        delaySlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            delayChannel.setDelay((double) source.getValue() / 100);
        });
        delayPanel.add(delaySlider);

        delayDecaySlider = new JSlider(JSlider.VERTICAL, 0, 100, (int) (delayDecay * 100));
        delayDecaySlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            delayChannel.setDecay((double) source.getValue() / 100);
        });
        delayPanel.add(delayDecaySlider);

        delayDryWetFactorSlider = new JSlider(JSlider.VERTICAL, 0, 99, (int) (dryWetFactor * 100));
        delayDryWetFactorSlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            delayChannel.setDryWetFactor((double) source.getValue() / 100);
        });
        delayPanel.add(delayDryWetFactorSlider);
    }

    private void configureLowPassFilterPanel() {
        lpfPanel.setBorder(BorderFactory.createTitledBorder("Low-Pass Filter"));

        lpfEnableCheckbox = new JCheckBox("enable", lpfEnabled);
        lpfEnableCheckbox.addChangeListener(e1 -> {
            JCheckBox source = (JCheckBox) e1.getSource();
            lpfChannel.setEnabled(source.isSelected());
        });
        lpfPanel.add(lpfEnableCheckbox);

        lpfCutOffFrequencySlider = new JSlider(JSlider.VERTICAL, 100, 10000, (int) (lpfCutoffFreq));
        lpfCutOffFrequencySlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            lpfChannel.setCutOffFrequency((double) source.getValue());
        });
        lpfPanel.add(lpfCutOffFrequencySlider);

        lpfResonanceSlider = new JSlider(JSlider.VERTICAL, 70, 1000, (int) (lpfQ * 100));
        lpfResonanceSlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            lpfChannel.setResonance((double) source.getValue() / 100);
        });
        lpfPanel.add(lpfResonanceSlider);

        lpfCutoffEnvelopeDepthSlider = new JSlider(JSlider.VERTICAL, 0, 10000, (int) (lpfCutoffEnvelopeDepth));
        lpfCutoffEnvelopeDepthSlider.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            lpfChannel.setCutoffEnvelopeDepth((double) source.getValue());
        });
        lpfPanel.add(lpfCutoffEnvelopeDepthSlider);
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
                volumeAdsrEnvelope.attack();
            }
            rootChannel.setFrequency(key.getNoteFrequency());
            currentFrequency = key.getNoteFrequency();
        } else {
            if (currentFrequency == 0 || currentFrequency == key.getNoteFrequency()) {
                volumeAdsrEnvelope.release();
                currentFrequency = 0;
            }
        }
    }

    private JSlider createVolumeSlider(Channel c) {
        JSlider volumeSquare = new JSlider(JSlider.VERTICAL, 0, 100, (int) (c.getVolume() * 100));
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
