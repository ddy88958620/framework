package com.handu.apollo.utils;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by markerking on 14/9/30.
 */
public final class ExtUtil {

    private ExtUtil() {}

    public static Map<String, Object> success() {
        return o2e(true, null);
    }

    public static Map<String, Object> success(Object o) {
        return o2e(true, o);
    }

    public static Map<String, Object> failure() {
        return o2e(false, null);
    }

    public static Map<String, Object> failure(Object o) {
        return o2e(false, o);
    }

    private static Map<String, Object> o2e(boolean success, Object o) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("success", success);
        if (o != null) {
            result.put("data", o);
        }
        return result;
    }
}
