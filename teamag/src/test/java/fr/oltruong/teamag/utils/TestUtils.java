package fr.oltruong.teamag.utils;

import java.lang.reflect.Field;

public class TestUtils {

    private TestUtils() {

    }

    public static void setPrivateAttribute(Object object, Object attribute, String name) {
        setPrivateAttribute(object, object.getClass(), attribute, name);

    }

    public static void setPrivateAttribute(Object object, @SuppressWarnings("rawtypes")
    Class className, Object attribute, String name) {
        try {
            Field field = className.getDeclaredField(name);
            field.setAccessible(true);
            field.set(object, attribute);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
