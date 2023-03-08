package com.momo.api.base.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

/**
 * *
 * Class JSONFormatter
 */
public final class JSONFormatter {

    /**
     * *
     * Default constructor
     */
    private JSONFormatter() {
    }

    // Set field naming policy
    private static FieldNamingPolicy FIELD_NAMING_POLICY = FieldNamingPolicy.IDENTITY;

    /**
     * Gson
     */
    public static Gson GSON = new GsonBuilder().setPrettyPrinting()
            .setFieldNamingPolicy(FIELD_NAMING_POLICY).create();

    /**
     * Set a format for gson FIELD_NAMING_POLICY
     *
     * @param FIELD_NAMING_POLICY
     */
    public static final void setFIELD_NAMING_POLICY(FieldNamingPolicy FIELD_NAMING_POLICY) {
        GSON = new GsonBuilder().setPrettyPrinting().setFieldNamingPolicy(FIELD_NAMING_POLICY).create();
    }

    /**
     * *
     * Converts a Raw Type to JSON String
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String toJSON(T t) {
        return GSON.toJson(t);
    }

    public static <T> String toJSONArray(List<T> t) {
        return GSON.toJsonTree(t).getAsJsonArray().toString();
    }

    /**
     * *
     * Converts a JSON String to object representation
     *
     * @param responseString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJSON(String responseString, Class<T> clazz) {
        T t = null;
        if (clazz.isAssignableFrom(responseString.getClass())) {
            t = clazz.cast(responseString);
        } else {
            t = GSON.fromJson(responseString, clazz);
        }
        return t;
    }

    /**
     * *
     *
     * @param responseString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJSONList(String responseString, Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        try {
            List<T> objectList = new Gson().fromJson(responseString.trim(), new TypeToken<List<T>>() {
            }.getType());
            for (T object : objectList) {
                resultList.add(GSON.fromJson(GSON.toJson(object), clazz));
            }
            return resultList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
