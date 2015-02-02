package com.handu.apollo.utils;

import java.util.Collection;

/**
 * Created by markerking on 14-5-29.
 */
public final class CollectionUtil {

    private CollectionUtil() {}

    public static boolean isNotEmpty(Collection collection) {
        return collection != null && !collection.isEmpty() && collection.size() > 0;
    }
}
