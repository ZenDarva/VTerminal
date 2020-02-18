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
        System.out.println("Nop");
        Color color = btn.getBackgroundHover();
        System.out.println(color);
    }
}
