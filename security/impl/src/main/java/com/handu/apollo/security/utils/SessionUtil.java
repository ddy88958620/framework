package com.handu.apollo.security.utils;

import com.handu.apollo.security.domain.SecurityUser;
import com.handu.apollo.utils.StringPool;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

/**
 * Created by markerking on 14-5-29.
 */
public class SessionUtil {
    public static Long getUserId() {
        return (Long) SecurityUtils.getSubject().getSession().getAttribute(StringPool.USER_PK);
    }

    public static SecurityUser getUser() {
        return (SecurityUser) SecurityUtils.getSubject().getSession().getAttribute(StringPool.USER);
    }

    public static Object get(Object key) {
        return SecurityUtils.getSubject().getSession().getAttribute(key);
    }

    public static void set(Object key, Object value) {
        SecurityUtils.getSubject().getSession().setAttribute(key, value);
    }

    public static void remove(Object key) {
        SecurityUtils.getSubject().getSession().removeAttribute(key);
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }
}
