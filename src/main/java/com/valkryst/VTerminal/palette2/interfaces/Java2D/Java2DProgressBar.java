package com.valkryst.VTerminal.palette2.interfaces.Java2D;

import java.awt.*;

public interface Java2DProgressBar extends Java2DPalette {


    public void setBackgroundIncomplete(Color color);
    public Color getBackgroundIncomplete();

    public void setForegroundIncomplete(Color color);
    public Color getForegroundIncomplete();
}
