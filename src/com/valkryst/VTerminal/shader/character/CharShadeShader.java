package com.valkryst.VTerminal.shader.character;

import com.valkryst.VTerminal.Tile;
import com.valkryst.VTerminal.GraphicTile;
import com.valkryst.VTerminal.misc.ColorFunctions;
import com.valkryst.VTerminal.shader.Shader;
import lombok.Data;
import lombok.NonNull;

import java.awt.Color;
import java.awt.image.BufferedImage;

@Data
public class CharShadeShader implements CharShader {
    /** Whether or not to shade the background color. */
    private boolean shadeBackground = false;
    /** Whether or not to shade the foreground color. */
    private boolean shadeForeground = false;
    /** The factor to shade the background color by. */
    private double backgroundShadeFactor = 0.0;
    /** The factor to shade the foreground color by. */
    private double foregroundShadeFactor = 0.0;

    @Override
    public BufferedImage run(@NonNull BufferedImage image, final @NonNull Tile character) {
        if (character instanceof GraphicTile) {
            return image;
        }

        if (shadeBackground) {
            final Color originalColor = character.getBackgroundColor();
            final Color shadeColor = ColorFunctions.shade(originalColor, backgroundShadeFactor);
            image = swapColor(image, originalColor, shadeColor);
        }

        if (shadeForeground) {
            final Color originalColor = character.getForegroundColor();
            final Color shadeColor = ColorFunctions.shade(originalColor, foregroundShadeFactor);
            image = swapColor(image, originalColor, shadeColor);
        }

        return image;
    }

    @Override
    public Shader copy() {
        final CharShadeShader shadeShader = new CharShadeShader();
        shadeShader.setShadeBackground(shadeBackground);
        shadeShader.setShadeForeground(shadeForeground);
        shadeShader.setBackgroundShadeFactor(backgroundShadeFactor);
        shadeShader.setForegroundShadeFactor(foregroundShadeFactor);
        return shadeShader;
    }
}
