package com.handu.apollo.data.utils;

import com.google.common.collect.Maps;
import com.handu.apollo.utils.StringPool;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by markerking on 14-5-22.
 */
public class Param {
    public static class Builder {
        private final Map<String, Object> params = Maps.newHashMap();
        public Builder put(String key, Object value) {
            params.put(key, value);
            return this;
        }

        public Builder putLikeLR(String key, String value) {
            if (StringUtils.isNotBlank(value)) {
                params.put(key, StringPool.PERCENT + value + StringPool.PERCENT);
            }
            return this;
        }

        public Builder putLikeL(String key, String value) {
            if (StringUtils.isNotBlank(value)) {
                params.put(key, StringPool.PERCENT + value);
            }
            return this;
        }

        public Builder putLikeR(String key, String value) {
            if (StringUtils.isNotBlank(value)) {
                params.put(key, value + StringPool.PERCENT);
            }
            return this;
        }

        public Map<String, Object> build() {
            return params;
        }
    }
}
