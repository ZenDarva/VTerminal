package com.valkryst.VTerminal.palette2;

import com.valkryst.VTerminal.misc.ColorFunctions;

import java.awt.*;

public class RawTileColor{

    public RawColor Foreground;
    public RawColor Background;

    public RawTileColor() {
        Foreground = new RawColor();
        Background = new RawColor();
    }

    public static class RawColor {
        public int[] RGBA;
        public float Shade;
        public float Tint;

        public RawColor() {
            RGBA = new int[4];
        }
        public Color toAWTColor(){

            Color color = new Color(RGBA[0],RGBA[1],RGBA[2],RGBA[3]);
            if (Shade !=0)
                color = ColorFunctions.shade(color,Shade);
            if (Tint !=0)
                color = ColorFunctions.tint(color,Tint);
            return color;
        }
    }

}

