package com.valkryst.VTerminal.palette2.interfaces.Java2D;


import java.awt.*;

public interface Java2DCheckBox extends Java2DPalette {

    public void setBackgroundHover(Color color);
    public Color getBackgroundHover();
    
    public void setForegroundHover(Color color);
    public Color getForegroundHover();

    public void setBackgroundPressed(Color color);
    public Color getBackgroundPressed();

    public void setForegroundPressed(Color color);
    public Color getForegroundPressed();
}
