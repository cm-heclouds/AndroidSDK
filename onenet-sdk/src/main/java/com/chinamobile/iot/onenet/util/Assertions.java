package com.chinamobile.iot.onenet.util;

public class Assertions {

    public static void assertUnreachable(String message) {
        throw new RuntimeException(message);
    }

    public static void assertCondition(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException(message);
        }
    }

    public static <T> T assertNotNull(T instance) {
        if (instance == null) {
            throw new RuntimeException("Expected object can not be null!");
        }
        return instance;
    }

}
