package com.valkryst.VTerminal.palette2.interfaces.Java2D;

import com.valkryst.VTerminal.palette2.PaletteProxy;

import java.awt.*;
import java.lang.reflect.Method;

public class Java2DProxy extends PaletteProxy {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (methodName.startsWith("get")) {
            String targName = methodName.replace("get","");
            return getValue(targName);
        }
        return null;
    }

    private Color getValue(String targName) {
        if (targName.contains("Foreground")){
            targName = targName.replace("Foreground","");
            if (targName.isEmpty()){
                return backingStore.get("Default").Foreground.toAWTColor();
            }
            return backingStore.get(targName).Foreground.toAWTColor();
        }
        if (targName.contains("Background")){
            targName = targName.replace("Background","");
            if (targName.isEmpty()){
                return backingStore.get("Default").Background.toAWTColor();
            }
            return backingStore.get(targName).Background.toAWTColor();
        }
        //We couldn't find anything.
        return Color.MAGENTA;
    }
}
