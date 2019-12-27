package com.synthesizer.javafx;

import com.synthesizer.AudioByteConverter;
import com.synthesizer.javafx.controller.GrandMotherController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;

import static com.synthesizer.javafx.util.AudioConstants.*;

public class GrandMother extends Application {
    SourceDataLine line;

    @Override
    public void start(Stage stage) throws LineUnavailableException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GrandMother.fxml"));

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        GrandMotherController grandMotherController = fxmlLoader.getController();

        stage.setScene(new Scene(fxmlLoader.getRoot()));
        stage.setTitle("Grandmother synthesizer");

        stage.show();

        startSynthAudio(grandMotherController);
    }

    @Override
    public void stop() {
        if (line != null && line.isOpen()) {
            line.drain();
            line.stop();
            line.close();
        }
    }

    private void startSynthAudio(GrandMotherController controller) throws LineUnavailableException {
        AudioByteConverter audioByteConverter = controller.getAudioByteConverter();
        AudioFormat audioFormat = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);
        line = AudioSystem.getSourceDataLine(audioFormat);
        line.open(audioFormat, 1024);
        line.start();

        new Thread(() -> {
            while (true) {
                byte[] mixBuffer = audioByteConverter.getByteArray();
                if (line.isOpen()) {
                    line.write(mixBuffer, 0, mixBuffer.length);
                } else {
                    System.exit(0);
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
