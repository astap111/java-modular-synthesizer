package com.synthesizer.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GrandMother extends Application {
    @Override
    public void start(Stage stage) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GrandMother.fxml"));

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        stage.setScene(new Scene(fxmlLoader.getRoot()));
        stage.setTitle("Grandmother synthesizer");

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
