package com.fido.tro.utils;

public class StringUtils {
    public static String ucfirst(String string) {
        return string.toUpperCase().substring(0, 1) + string.toLowerCase().substring(1);
    }
}
