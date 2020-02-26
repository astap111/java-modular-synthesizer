package com.synthesizer.javafx.controller;

import com.synthesizer.javafx.util.OctaveKnob;
import com.synthesizer.javafx.util.PresetsFileReader;
import com.synthesizer.javafx.util.WaveformKnob;
import com.synthesizer.model.GrandMotherPreset;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PresetsPaneController implements Initializable {
    @FXML
    private ComboBox<GrandMotherPreset> presetsComboBox;
    private List<GrandMotherPreset> presetList;
    private GrandMotherController grandMotherController;
    private EnvelopePaneController envelopePaneController;
    private OscillatorsPaneController oscillatorsPaneController;
    private MixerPaneController mixerPaneController;
    private FilterPaneController filterPaneController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        presetList = PresetsFileReader.readAllFromFile();
        if (presetList != null) {
            presetsComboBox.setItems(FXCollections.observableArrayList(presetList));
        }

        presetsComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            grandMotherController.getOutputVolume().setValue(newValue.outputVolume);
            envelopePaneController.getEnvelopeAttack().setValue(newValue.envelopeAttack);
            envelopePaneController.getEnvelopeDecay().setValue(newValue.envelopeDecay);
            envelopePaneController.getEnvelopeSustain().setValue(newValue.envelopeSustain);
            envelopePaneController.getEnvelopeRelease().setValue(newValue.envelopeRelease);
            oscillatorsPaneController.getOscillator1Waveform().setValue(WaveformKnob.valueOf(newValue.oscillator1Waveform));
            oscillatorsPaneController.getOscillator2Waveform().setValue(WaveformKnob.valueOf(newValue.oscillator2Waveform));
            oscillatorsPaneController.getOscillator1Octave().setValue(OctaveKnob.valueOf(newValue.oscillator1Octave));
            oscillatorsPaneController.getOscillator2Octave().setValue(OctaveKnob.valueOf(newValue.oscillator2Octave));
            oscillatorsPaneController.getDetuneFrequency().setValue(newValue.detuneFrequency);
            oscillatorsPaneController.getOscillatorSync().setSelected(newValue.oscillatorSync);
            mixerPaneController.getOscillator1Volume().setValue(newValue.oscillator1Volume);
            mixerPaneController.getOscillator2Volume().setValue(newValue.oscillator2Volume);
            mixerPaneController.getNoiseVolume().setValue(newValue.noiseVolume);
            filterPaneController.getFilterCutoff().setValue(newValue.filterCutoff);
            filterPaneController.getFilterEnvelopeAmt().setValue(newValue.filterEnvelopeAmt);
            filterPaneController.getFilterResonance().setValue(newValue.filterResonance);
            filterPaneController.getKbdTrack().setValue(newValue.kbdTrack);
        });
    }

    public void postInitialize(GrandMotherController grandMotherController,
                               EnvelopePaneController envelopePaneController,
                               OscillatorsPaneController oscillatorsPaneController,
                               MixerPaneController mixerPaneController,
                               FilterPaneController filterPaneController) {
        this.grandMotherController = grandMotherController;
        this.envelopePaneController = envelopePaneController;
        this.oscillatorsPaneController = oscillatorsPaneController;
        this.mixerPaneController = mixerPaneController;
        this.filterPaneController = filterPaneController;
    }
}
