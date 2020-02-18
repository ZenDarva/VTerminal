package com.valkryst.VTerminal.palette2.interfaces;

import java.lang.reflect.InvocationHandler;

public interface Palette {
//All palette interfaces must inherit from this class, directly or indirectly.

    //All methods in this interface exist only for inheritance reasons, to maintain the same interface for all palettes.

    //This should be hidden by a static method in the top level palette for a category of palettes.
    //It's used to determine which proxy should be used by the PaletteFactory.
    static Class<? extends InvocationHandler> getInvocationHandler(){return null;}

    //All palette interfaces should have the same prefix, so that the PaletteFactory can examine the class name
    //to determine which component it is a palette for.
    //This should be in the form <prefix><component name>, such as "Java2DButton", or "JavaFXButton"
    //This should also be hidden by a static method int he top level palette for a category of palettes.
    static String getPrefix(){return null;}
}
