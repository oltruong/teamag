package fr.oltruong.teamag.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    public static Object callPrivateMethod(Object object, String methodName, Object... arguments) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = object.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(object,arguments);
    }
}
