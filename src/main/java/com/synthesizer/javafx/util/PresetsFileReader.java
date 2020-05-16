package com.synthesizer.javafx.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.synthesizer.model.GrandMotherPreset;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class PresetsFileReader {
    public static List<GrandMotherPreset> readAllFromFile() {
        Gson gson = new Gson();
        Type presetListType = new TypeToken<List<GrandMotherPreset>>() {
        }.getType();
        List<GrandMotherPreset> presets = null;
        try {
            presets = gson.fromJson(new FileReader("presets.json"), presetListType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return presets;
    }
}
