package com.valkryst.VTerminal.shader.character;

import com.valkryst.VTerminal.Tile;
import com.valkryst.VTerminal.GraphicTile;
import com.valkryst.VTerminal.shader.Shader;
import lombok.NonNull;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class CharBoldShader implements CharShader {
    @Override
    public BufferedImage run(final @NonNull BufferedImage image, final @NonNull Tile character) {
        if (character instanceof GraphicTile) {
            return image;
        }

        if (character.isForegroundAndBackgroundColorEqual()) {
            return image;
        }

        try {
            // Get the character image:
            final BufferedImage charImage = swapColor(image, character.getBackgroundColor(), new Color(0, 0, 0, 0));

            // Combine image and background
            final BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            final Graphics2D gc = (Graphics2D) result.getGraphics();

            gc.setColor(character.getBackgroundColor());
            gc.fillRect(0, 0, result.getWidth(), result.getHeight());
            gc.drawImage(charImage, 1, 0, null);
            gc.drawImage(charImage, 0, 0, null);
            gc.dispose();

            return result;
        } catch (final IllegalStateException e) {
            return image;
        }
    }

    @Override
    public Shader copy() {
        return new CharBoldShader();
    }
}
