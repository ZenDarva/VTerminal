package com.valkryst.VTerminal.palette2.interfaces.Java2D;

import com.valkryst.VTerminal.palette2.PaletteProxy;

import java.awt.*;
import java.lang.reflect.Method;

public class Java2DProxy extends PaletteProxy {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //Method name will look something like "getForegroundHover", or "setBackgroundPressed"
        //First, we shave off the get/set, to know which type of method we should emulate.

        String methodName = method.getName();

        if (methodName.startsWith("get")) {
            String targName = methodName.replace("get","");
            return getValue(targName);
        }
        if (methodName.startsWith("set")){
            String targName = methodName.replace("set","");
            return setValue(targName, args);
        }
        return null;
    }

    private Object setValue(String targName, Object[] args) {
        if (!(args[0] instanceof Color)){
            throw new IllegalArgumentException("Passed something other than a Color to a Java2DProxy set Method");
        }
        Color color = (Color) args[0];

        if (targName.contains("Foreground")){
            targName = targName.replace("Foreground","");
            if (targName.isEmpty()){ //If it's empty, the method called was setForeground, and we'll want to set the default value.
                    backingStore.get("Default").Foreground.fromAWTColor(color);
            }
            else
                backingStore.get(targName).Foreground.fromAWTColor(color);  //Set the appropriate value in the backingStore. i.e. Hover, or Pressed.

        }
        if (targName.contains("Background")){
            targName = targName.replace("Background","");
            if (targName.isEmpty()){
                backingStore.get("Default").Background.fromAWTColor(color);
            }
            else
                backingStore.get(targName).Background.fromAWTColor(color);

        }
        return null;//Got to love reflection.  Well, we have to return something.
    }

    private Color getValue(String targName) {
        if (targName.contains("Foreground")){
            targName = targName.replace("Foreground","");
            if (targName.isEmpty()){
                return backingStore.get("Default").Foreground.toAWTColor();
            }
            else
                return backingStore.get(targName).Foreground.toAWTColor();
        }
        if (targName.contains("Background")){
            targName = targName.replace("Background","");
            if (targName.isEmpty()){
                return backingStore.get("Default").Background.toAWTColor();
            }
            else
                return backingStore.get(targName).Background.toAWTColor();
        }
        //We couldn't find anything.
        return Color.MAGENTA;
    }
}
