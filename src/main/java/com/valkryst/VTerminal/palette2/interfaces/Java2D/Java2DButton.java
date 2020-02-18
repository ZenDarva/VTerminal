package com.valkryst.VTerminal.palette2.interfaces.Java2D;

import com.valkryst.VTerminal.palette2.interfaces.Palette;

import java.awt.*;

public interface Java2DButton extends Java2DPalette {
    void setBackgroundHover(Color color);
    Color getBackgroundHover();

    void setForegroundHover(Color color);
    Color getForegroundHover();

    void setBackgroundPressed(Color color);
    Color getBackgroundPressed();

    void setForegroundPressed(Color color);
    Color getForegroundPressed();
}
