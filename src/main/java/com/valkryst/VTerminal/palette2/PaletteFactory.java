package com.valkryst.VTerminal.palette2;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.valkryst.VTerminal.palette2.interfaces.Java2D.Java2DProxy;
import com.valkryst.VTerminal.palette2.interfaces.Palette;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Map;

public class PaletteFactory {


    public static Palette getPallate(Class<? extends Palette> clazz, String name){
        Map<String, Map<String,RawTileColor>> backingStore;
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        File file = new File("src/main/resources/Palettes/PaleNight.json");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);
        JsonReader reader = new JsonReader(isr);
        Type type = new TypeToken<Map<String, Map<String, RawTileColor>>>(){}.getType();
        backingStore = gson.fromJson(isr, type);

        PaletteProxy proxy = instantiateProxy(clazz);
        proxy.setBackingStore(cloneBackingStore(backingStore.get(getType(clazz))));

        Palette paletteProxy = (Palette) Proxy.newProxyInstance(clazz.getClassLoader(),getInterfaces(clazz),proxy);

        return paletteProxy;
    }

    @SneakyThrows
    private static PaletteProxy instantiateProxy(Class<? extends Palette> clazz){
        Method method = null;
        for (Class<?> anInterface : clazz.getInterfaces()) {
            if (Palette.class.isAssignableFrom(anInterface)){
                method = anInterface.getMethod("getInvocationHandler",null);
            }
        }
        if (method == null){
            throw new IllegalArgumentException("Attempted to create a palette for a class that is not a child of Palette.");
        }

        Class outClass = (Class) method.invoke(null);

        Constructor targConstructor = null;
        for (Constructor constructor : outClass.getConstructors()) {
            if (constructor.getParameterCount() == 0){
                targConstructor = constructor;
            }
        }
        if (targConstructor == null){// || !targConstructor.isAccessible()){
            throw new IllegalArgumentException("Attempted to instantiate a class that does not have a 0 args constructor.");
        }
        return (PaletteProxy) targConstructor.newInstance();
    }
    private static Class[] getInterfaces(Class<? extends Palette> targ){
        Class[] interfaces = new Class[targ.getInterfaces().length+1];
        for (int i = 0; i < targ.getInterfaces().length; i++) {
            interfaces[i]=targ.getInterfaces()[i];
        }
        interfaces[targ.getInterfaces().length]=targ;
        return interfaces;
    }
    @SneakyThrows
    private static String getType(Class<? extends Palette> clazz){
        Method method = null;
        for (Class<?> anInterface : clazz.getInterfaces()) {
            if (Palette.class.isAssignableFrom(anInterface)){
                method = anInterface.getMethod("getPrefix",null);
            }
        }
        if (method == null){
            throw new IllegalArgumentException("Attempted to create a palette for a class that is not a child of Palette.");
        }
        String prefix = (String) method.invoke(null);
        return clazz.getSimpleName().replace(prefix,"");
    }

    private static Map<String, RawTileColor> cloneBackingStore(Map<String, RawTileColor> targ){
        Type type = new TypeToken<Map<String, RawTileColor>>(){}.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.fromJson(gson.toJson(targ),type);
    }
}
