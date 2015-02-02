package com.handu.apollo.utils;

import com.handu.apollo.config.service.easyzk.ConfigNode;
import org.springframework.core.env.Environment;

import java.util.regex.Pattern;

/**
 * Created by markerking on 14-4-3.
 */
public final class StringUtil {

    private StringUtil() {}

    // 移除请求中的password
    private static final Pattern REGEX_PASSWORD_QUERYSTRING = Pattern.compile("&?(password|accesskey|secretkey)=.*?(?=[&'\"])");

    // 移除JSON中的password/accesskey/secretkey
    private static final Pattern REGEX_PASSWORD_JSON = Pattern.compile("\"(password|accesskey|secretkey)\":\".*?\",?");

    public static String cleanString(String stringToClean){
        String cleanResult = "";
        if (stringToClean != null) {
            cleanResult = REGEX_PASSWORD_QUERYSTRING.matcher(stringToClean).replaceAll("");
            cleanResult = REGEX_PASSWORD_JSON.matcher(cleanResult).replaceAll("");
        }
        return cleanResult;
    }

    public static String getProperty(ConfigNode configNode, Environment env, String key) {
        return getProperty(configNode, env, key, null);
    }

    public static String getProperty(ConfigNode configNode, Environment env, String key, String defaultValue) {
        if (configNode == null) {
            return env.getProperty(key, defaultValue);
        }
        return configNode.getProperty(key, defaultValue);
    }
}
