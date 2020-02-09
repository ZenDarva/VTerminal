//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.valkryst.VJSON;

import java.awt.Color;
import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface VJSONParser {
    void parse(@NonNull JSONObject var1);

    default void checkType(@NonNull JSONObject jsonObject, @NonNull String expectedType) {
        if (jsonObject == null) {
            throw new NullPointerException("jsonObject");
        } else if (expectedType == null) {
            throw new NullPointerException("expectedType");
        } else if (expectedType.isEmpty()) {
            throw new IllegalStateException("The type of the object cannot be empty.\n" + jsonObject.toJSONString());
        } else {
            String type = this.getString(jsonObject, "type");
            if (type == null) {
                throw new IllegalStateException("The JSON object has no type value.\n" + jsonObject.toJSONString());
            } else if (!type.equals(expectedType)) {
                throw new IllegalStateException("The type '" + type + "' does not match the expected type '" + expectedType + "'.\n" + jsonObject.toJSONString());
            }
        }
    }

    default Byte getByte(@NonNull JSONObject jsonObject, @NonNull String element) {
        if (jsonObject == null) {
            throw new NullPointerException("jsonObject");
        } else if (element == null) {
            throw new NullPointerException("element");
        } else if (element.isEmpty()) {
            throw new IllegalArgumentException("You must specify an element.");
        } else {
            Object object = jsonObject.get(element);
            return object != null ? ((Number)object).byteValue() : null;
        }
    }

    default Short getShort(@NonNull JSONObject jsonObject, @NonNull String element) {
        if (jsonObject == null) {
            throw new NullPointerException("jsonObject");
        } else if (element == null) {
            throw new NullPointerException("element");
        } else if (element.isEmpty()) {
            throw new IllegalArgumentException("You must specify an element.");
        } else {
            Object object = jsonObject.get(element);
            return object != null ? ((Number)object).shortValue() : null;
        }
    }

    default Integer getInteger(@NonNull JSONObject jsonObject, @NonNull String element) {
        if (jsonObject == null) {
            throw new NullPointerException("jsonObject");
        } else if (element == null) {
            throw new NullPointerException("element");
        } else if (element.isEmpty()) {
            throw new IllegalArgumentException("You must specify an element.");
        } else {
            Object object = jsonObject.get(element);
            return object != null ? ((Number)object).intValue() : null;
        }
    }

    default Long getLong(@NonNull JSONObject jsonObject, @NonNull String element) {
        if (jsonObject == null) {
            throw new NullPointerException("jsonObject");
        } else if (element == null) {
            throw new NullPointerException("element");
        } else if (element.isEmpty()) {
            throw new IllegalArgumentException("You must specify an element.");
        } else {
            Object object = jsonObject.get(element);
            return object != null ? ((Number)object).longValue() : null;
        }
    }

    default Float getFloat(@NonNull JSONObject jsonObject, @NonNull String element) {
        if (jsonObject == null) {
            throw new NullPointerException("jsonObject");
        } else if (element == null) {
            throw new NullPointerException("element");
        } else if (element.isEmpty()) {
            throw new IllegalArgumentException("You must specify an element.");
        } else {
            Object object = jsonObject.get(element);
            return object != null ? ((Number)object).floatValue() : null;
        }
    }

    default Double getDouble(@NonNull JSONObject jsonObject, @NonNull String element) {
        if (jsonObject == null) {
            throw new NullPointerException("jsonObject");
        } else if (element == null) {
            throw new NullPointerException("element");
        } else if (element.isEmpty()) {
            throw new IllegalArgumentException("You must specify an element.");
        } else {
            Object object = jsonObject.get(element);
            return object != null ? ((Number)object).doubleValue() : null;
        }
    }

    default Character getChar(@NonNull JSONObject jsonObject, @NonNull String element) {
        if (jsonObject == null) {
            throw new NullPointerException("jsonObject");
        } else if (element == null) {
            throw new NullPointerException("element");
        } else if (element.isEmpty()) {
            throw new IllegalArgumentException("You must specify an element.");
        } else {
            Object object = jsonObject.get(element);
            return object != null ? ((String)object).charAt(0) : null;
        }
    }

    default String getString(@NonNull JSONObject jsonObject, @NonNull String element) {
        if (jsonObject == null) {
            throw new NullPointerException("jsonObject");
        } else if (element == null) {
            throw new NullPointerException("element");
        } else if (element.isEmpty()) {
            throw new IllegalArgumentException("You must specify an element.");
        } else {
            Object object = jsonObject.get(element);
            return object != null ? (String)object : null;
        }
    }

    default Color getColor(@NonNull JSONArray jsonArray) {
        if (jsonArray == null) {
            throw new NullPointerException("jsonArray");
        } else if (jsonArray.size() >= 3) {
            Integer red = (int)jsonArray.get(0);
            Integer green = (int)jsonArray.get(1);
            Integer blue = (int)jsonArray.get(2);
            Integer alpha = jsonArray.size() >= 4 ? (int)jsonArray.get(3) : 255;
            return new Color(red, green, blue, alpha);
        } else {
            throw new IllegalStateException("Cannot load a color with fewer than 3 values.");
        }
    }
}
