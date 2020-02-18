package com.valkryst.VTerminal.palette2.interfaces.Java2D;

import java.awt.*;

public interface Java2DTextArea extends Java2DPalette {
    
    void setBackgroundCaret(Color color);
    Color getBackgroundCaret();
    
    void setForegroundCaret(Color color);
    Color getForegroundCaret();
}
