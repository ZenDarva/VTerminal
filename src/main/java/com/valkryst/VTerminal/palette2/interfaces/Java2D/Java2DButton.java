package com.valkryst.VTerminal.palette2.interfaces.Java2D;

import com.valkryst.VTerminal.palette2.interfaces.Palette;

import java.awt.*;

public interface Java2DButton extends Java2DPalette {
    void setBackgroundHover(final int rgba);
    Color getBackgroundHover();

    void setForegroundHover(final int rgba);
    Color getForegroundHover();

    void setBackgroundPressed(final int rgba);
    Color getBackgroundPressed();

    void setForegroundPressed(final int rgba);
    Color getForegroundPressed();
}
