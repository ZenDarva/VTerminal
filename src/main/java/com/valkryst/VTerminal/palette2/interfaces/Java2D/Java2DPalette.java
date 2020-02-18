package com.valkryst.VTerminal.palette2.interfaces.Java2D;

import com.valkryst.VTerminal.palette2.interfaces.Palette;

import java.awt.*;
import java.lang.reflect.InvocationHandler;

//This is the top level Palette for Java2d.


public interface Java2DPalette extends Palette {

    //These two methods hide the static methods on Palette.
    static Class<? extends InvocationHandler> getInvocationHandler(){
        return Java2DProxy.class;
    }
    static String getPrefix(){
        return "Java2D";
    }



    public Color getForeground();
    void setForeground(Color color);
    Color getBackground();
    void setBackground(Color color);

}
