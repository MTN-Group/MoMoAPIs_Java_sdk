package com.momo.api.base.util;

/**
 * *
 * Class StringUtils
 */
public class StringUtils {

    /**
     * Returns true if String is null or empty 
     *
     * @param stringValue
     * @return
     */
    public static boolean isNullOrEmpty(final String stringValue) {
        return stringValue == null || stringValue.trim().isEmpty();
    }
}
