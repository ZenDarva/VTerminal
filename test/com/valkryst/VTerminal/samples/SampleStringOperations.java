package com.valkryst.VTerminal.samples;

import com.valkryst.VTerminal.Panel;
import com.valkryst.VTerminal.builder.PanelBuilder;
import com.valkryst.VTerminal.builder.component.LabelBuilder;
import com.valkryst.VTerminal.component.Label;
import com.valkryst.VTerminal.component.Screen;
import com.valkryst.VTerminal.font.Font;
import com.valkryst.VTerminal.font.FontLoader;
import com.valkryst.VTerminal.shader.FlipShader;

import java.awt.Color;
import java.io.IOException;
import java.net.URISyntaxException;

public class SampleStringOperations {
    public static void main(final String[] args) throws IOException, URISyntaxException, InterruptedException {
        final Font font = FontLoader.loadFontFromJar("Fonts/DejaVu Sans Mono/18pt/bitmap.png", "Fonts/DejaVu Sans Mono/18pt/data.fnt", 1);

        final PanelBuilder builder = new PanelBuilder();
        builder.setFont(font);

        final Panel panel = builder.build();

        final Screen screen = panel.getScreen();

        Thread.sleep(50);

        final LabelBuilder labelBuilder = new LabelBuilder();
        labelBuilder.setColumnIndex(0);
        labelBuilder.setRowIndex(0);


        labelBuilder.setText("Writing a regular string.");
        Label label = labelBuilder.build();
        screen.addComponent(label);


        labelBuilder.setRowIndex(labelBuilder.getRowIndex() + 1);
        labelBuilder.setText("Writing a horizontally flipped string.");
        label = labelBuilder.build();
        label.addShaders(new FlipShader(true, false));
        screen.addComponent(label);


        labelBuilder.setRowIndex(labelBuilder.getRowIndex() + 1);
        labelBuilder.setText("Writing a vertically flipped string.");
        label = labelBuilder.build();
        label.addShaders(new FlipShader(false, true));
        screen.addComponent(label);


        labelBuilder.setRowIndex(labelBuilder.getRowIndex() + 1);
        labelBuilder.setText("Writing a horizontally & vertically flipped string.");
        label = labelBuilder.build();
        label.addShaders(new FlipShader(true, true));
        screen.addComponent(label);


        labelBuilder.setRowIndex(labelBuilder.getRowIndex() + 1);
        labelBuilder.setText("Writing a string with a tint gradient.");
        label = labelBuilder.build();
        label.getString(0).applyTintGradient(true);
        screen.addComponent(label);


        labelBuilder.setRowIndex(labelBuilder.getRowIndex() + 1);
        labelBuilder.setText("Writing a string with a shade gradient.");
        label = labelBuilder.build();
        label.getString(0).applyShadeGradient(true);
        screen.addComponent(label);


        labelBuilder.setRowIndex(labelBuilder.getRowIndex() + 1);
        labelBuilder.setText("Writing a string with a background color gradient.");
        label = labelBuilder.build();
        label.getString(0).applyColorGradient(Color.CYAN, Color.PINK, true);
        screen.addComponent(label);


        labelBuilder.setRowIndex(labelBuilder.getRowIndex() + 1);
        labelBuilder.setText("Writing a string with a foreground color gradient.");
        label = labelBuilder.build();
        label.getString(0).applyColorGradient(Color.CYAN, Color.PINK, false);
        screen.addComponent(label);


        labelBuilder.setRowIndex(labelBuilder.getRowIndex() + 1);
        labelBuilder.setText("Writing a string with a fore/background color gradient.");
        label = labelBuilder.build();
        label.getString(0).applyColorGradient(Color.RED, Color.GREEN, false);
        label.getString(0).applyColorGradient(Color.CYAN, Color.PINK, true);
        screen.addComponent(label);


        labelBuilder.setRowIndex(labelBuilder.getRowIndex() + 1);
        labelBuilder.setText("Writing a string with a different background color.");
        label = labelBuilder.build();
        label.getString(0).setBackgroundColor(Color.RED);
        screen.addComponent(label);


        labelBuilder.setRowIndex(labelBuilder.getRowIndex() + 1);
        labelBuilder.setText("Writing a string with a different foreground color.");
        label = labelBuilder.build();
        label.getString(0).setForegroundColor(Color.RED);
        screen.addComponent(label);


        labelBuilder.setRowIndex(labelBuilder.getRowIndex() + 1);
        labelBuilder.setText("Writing a string with a different back/foreground color.");
        label = labelBuilder.build();
        label.getString(0).setForegroundColor(Color.RED);
        label.getString(0).setBackgroundColor(Color.BLUE);
        screen.addComponent(label);


        labelBuilder.setRowIndex(labelBuilder.getRowIndex() + 1);
        labelBuilder.setText("Writing an underlined string.");
        label = labelBuilder.build();
        label.getString(0).setUnderlined(true);
        screen.addComponent(label);

        panel.draw();
    }
}
