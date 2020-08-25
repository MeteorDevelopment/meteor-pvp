package minegame159.thebestplugin.utils;

import java.lang.reflect.Field;

public class Reflection {
    public static Field getField(Class<?> klass, String name) {
        try {
            Field field = klass.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setField(String name, Object owner, Object value) {
        try {
            getField(owner.getClass(), name).set(owner, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
