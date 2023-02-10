package com.momo.api.base;

import java.util.HashMap;
import java.util.Map;

/***
 * Class SDKUtil
 */
public final class SDKUtil {
    /***
     * Private constructor
     */
    private SDKUtil() { }

    /***
     *
     * @param receivedMap
     * @return
     */
    public static Map<String, String> combineDefaultMap(Map<String, String> receivedMap) {
        return combineMap(receivedMap, ConfigManager.getDefaultSDKMap());
    }

    /***
     *
     * @param highMap
     * @param lowMap
     * @return
     */
    public static Map<String, String> combineMap(Map<String, String> highMap, Map<String, String> lowMap) {
        lowMap = lowMap != null ? lowMap : new HashMap<>();
        highMap = highMap != null ? highMap : new HashMap<>();
        lowMap.putAll(highMap);
        return lowMap;
    }
}

