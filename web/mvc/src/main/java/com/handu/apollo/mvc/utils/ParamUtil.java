package com.handu.apollo.mvc.utils;

import com.handu.apollo.utils.GetterUtil;
import com.handu.apollo.base.Page;
import com.handu.apollo.utils.StringPool;
import com.handu.apollo.utils.ValidateUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by markerking on 14/8/13.
 */
public class ParamUtil {

    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static Date get(HttpServletRequest request, String year, String month, String day, Date defaultValue) {
        year = get(request, year, "");
        month = get(request, month, "");
        day = get(request, day, "");

        StringBuffer format = new StringBuffer();
        if (ValidateUtil.isNotNull(year)) {
            format.append("yyyy");
        }
        if (ValidateUtil.isNotNull(month)) {
            format.append("MM");
        }
        if (ValidateUtil.isNotNull(day)) {
            format.append("dd");
        }

        if (format.toString().equals(StringPool.BLANK)) {
            return defaultValue;
        }

        SimpleDateFormat formater = new SimpleDateFormat(format.toString());

        try {
            return formater.parse(year + month + day);
        } catch (ParseException e) {
            return defaultValue;
        }
    }

    public static boolean get(HttpServletRequest request, String param, boolean defaultValue) {
        return GetterUtil.get(request.getParameter(param), defaultValue);
    }

    public static double get(HttpServletRequest request, String param, double defaultValue) {
        return GetterUtil.get(request.getParameter(param), defaultValue);
    }

    public static float get(HttpServletRequest request, String param, float defaultValue) {
        return GetterUtil.get(request.getParameter(param), defaultValue);
    }

    public static int get(HttpServletRequest request, String param, int defaultValue) {
        return GetterUtil.get(request.getParameter(param), defaultValue);
    }

    public static long get(HttpServletRequest request, String param, long defaultValue) {
        return GetterUtil.get(request.getParameter(param), defaultValue);
    }

    public static short get(HttpServletRequest request, String param, short defaultValue) {
        return GetterUtil.get(request.getParameter(param), defaultValue);
    }

    public static String get(HttpServletRequest request, String param, String defaultValue) {
        if (param == null) {
            return defaultValue;
        }
        return GetterUtil.get(request.getParameter(param), defaultValue);
    }

    public static String[] gets(HttpServletRequest request, String param, String[] defaultValue) {
        String[] returnValues = request.getParameterValues(param);
        if (returnValues == null) {
            return defaultValue;
        }
        return returnValues;
    }

    public static String[] get(HttpServletRequest request, String param, String splitor, String[] defaultValue) {
        if (param == null) {
            return defaultValue;
        }
        String[] returnValues = get(request, param, StringPool.BLANK).split(splitor, -1);
        if (returnValues == null || returnValues.length == 0) {
            return defaultValue;
        }
        return returnValues;
    }

    public static Page getPager(HttpServletRequest request){
        Page page = new Page();

        page.setPage(GetterUtil.get(request.getParameter("page"), 1));
        page.setPagesize(GetterUtil.get(request.getParameter("limit"), 20));

        return page;
    }
}
