package com.valkryst.VTerminal.palette2;

import java.lang.reflect.InvocationHandler;
import java.util.Map;

public abstract class PaletteProxy implements InvocationHandler {

    //This stores the internal representation of colors, and which portions they apply to.
    //It is cloned from the master stored in PaletteFactory, so changes are local to the object they're changed on.
    protected Map<String,RawTileColor> backingStore;

    protected void setBackingStore(Map<String, RawTileColor> backingStore){
        this.backingStore=backingStore;
    }

    @Override
    public String toString() {
        //Null looked stupid in the Debug window. *shrug*
        return "PaletteProxy";
    }
}
