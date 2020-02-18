package com.valkryst.VTerminal.palette2;

import com.valkryst.VTerminal.palette2.interfaces.Java2D.Java2DButton;

import java.awt.*;
import java.io.IOException;

public class PaletteTest {
    public static void main(String[] args) throws IOException {
        testFactory();
    }

    private static void testFactory(){
        Java2DButton btn = (Java2DButton) PaletteFactory.getPallate(Java2DButton.class, "PaleNight");

        Color backgroundHover = btn.getBackgroundHover();
        if (backgroundHover.equals(new Color(176,135,223,255))){
            System.out.println("BackgroundHover matches.");
        }
        Color defaultBackground = btn.getBackground();
        if (defaultBackground.equals(new Color (41,45,62,255))){
            System.out.println("DefaultBackground matches.");
        }
        btn.getForeground();

        btn.setForeground(Color.BLACK);
        if (btn.getForeground().equals(Color.BLACK)){
            System.out.println("Successfully set ForegroundHover");
        }
    }
}

