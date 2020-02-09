//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.valkryst.VJSON;

import com.sun.javafx.tk.FontLoader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VJSONLoader {
    public VJSONLoader() {
    }

    public static void loadFromJSON(@NonNull VJSONParser parser, @NonNull String jsonFilePath) throws ParseException, IOException {
        if (parser == null) {
            throw new NullPointerException("parser");
        } else if (jsonFilePath == null) {
            throw new NullPointerException("jsonFilePath");
        } else if (jsonFilePath.isEmpty()) {
            throw new IllegalArgumentException("The JSON file path cannot be empty.");
        } else {
            loadFromJSON(parser, (InputStream)(new FileInputStream(jsonFilePath)));
        }
    }

    public static void loadFromJSONInJar(@NonNull VJSONParser parser, @NonNull String jsonFilePath) throws ParseException, IOException {
        if (parser == null) {
            throw new NullPointerException("parser");
        } else if (jsonFilePath == null) {
            throw new NullPointerException("jsonFilePath");
        } else if (jsonFilePath.isEmpty()) {
            throw new IllegalArgumentException("The JSON file path cannot be empty.");
        } else {
            ClassLoader classLoader = FontLoader.class.getClassLoader();
            InputStream jsonFileStream = classLoader.getResourceAsStream(jsonFilePath);
            loadFromJSON(parser, jsonFileStream);
            jsonFileStream.close();
        }
    }

    public static void loadFromJSON(@NonNull VJSONParser parser, @NonNull InputStream jsonFileStream) throws ParseException, IOException {
        if (parser == null) {
            throw new NullPointerException("parser");
        } else if (jsonFileStream == null) {
            throw new NullPointerException("jsonFileStream");
        } else {
            InputStreamReader isr = new InputStreamReader(jsonFileStream, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            List<String> lines = (List)br.lines().collect(Collectors.toList());
            String jsonData = String.join("\n", lines);
            br.close();
            isr.close();
            parser.parse((JSONObject)(new JSONParser()).parse(jsonData));
        }
    }
}
