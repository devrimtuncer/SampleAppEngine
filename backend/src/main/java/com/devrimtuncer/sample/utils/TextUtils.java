package com.devrimtuncer.sample.utils;

/**
 * Created by devrimtuncer on 28/03/16.
 */
public abstract class TextUtils {
    public static boolean isNullOrEmpty (String value) {
        return value == null || "".equals(value.trim());
    }
}
