package com.valkryst.VTerminal.palette2;

import java.lang.reflect.InvocationHandler;
import java.util.Map;

public abstract class PaletteProxy implements InvocationHandler {

    public Map<String,RawTileColor> backingStore;

    protected void setBackingStore(Map<String, RawTileColor> backingStore){
        this.backingStore=backingStore;
    }

}
