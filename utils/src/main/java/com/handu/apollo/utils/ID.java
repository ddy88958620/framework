package com.handu.apollo.utils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by markerking on 14-4-14.
 */
public class ID {

    private static final int LENGTH = 12;
    private static final String FORMAT = "yyyyMMdd";

    //static method only
    private ID() {}

    /**
     * 默认12位补齐
     * 例如：ID.gen(10) 补齐为000000000010
     * @param id
     * @return
     * */
    public static String gen(Serializable id) {
        return gen(id, LENGTH);
    }

    /**
     * 指定位数补齐
     * 例如：ID.gen(10, 5) 补齐为00010
     * @param id
     * @param length
     * @return
     * */
    public static String gen(Serializable id, int length) {
        StringBuffer fix = new StringBuffer();
        for(;length > 0; length--) {
            fix.append(CharPool.NUMBER_0);
        }
        fix.append(id);
        return fix.substring(id.toString().length(), fix.length());
    }

    /**
     * 指定前缀默认12位补齐
     * 例如：ID.gen(10, "SL") 补齐为LS000000000010
     * @param id
     * @param prefix
     * @return
     * */
    public static String gen(Serializable id, String prefix) {
        return gen(id, prefix, LENGTH);
    }

    /**
     * 指定前缀、指定位数补齐
     * 例如：ID.gen(10, "SL") 补齐为LS000000000010
     * @param id
     * @param prefix
     * @return
     * */
    public static String gen(Serializable id, String prefix, int length) {
        return prefix + gen(id, length);
    }

    /**
     * 默认12位、当前日期(yyyyMMdd)补齐
     * 例如：ID.date(10) 补齐为20140303000000000010
     * @param id
     * @return
     * */
    public static String date(Serializable id) {
        return date(id, LENGTH);
    }

    /**
     * 指定位数、当前日期(yyyyMMdd)补齐
     * 例如：ID.date(10, 5) 补齐为2014030300010
     * @param id
     * @param length
     * @return
     * */
    public static String date(Serializable id, int length) {
        return date(id, new Date(), length);
    }

    /**
     * 默认12位、指定日期(yyyyMMdd)补齐
     * 例如：ID.date(10, date) 补齐为2014030300010
     * @param id
     * @param date
     * @return
     * */
    public static String date(Serializable id, Date date) {
        return date(id, date, FORMAT, LENGTH);
    }

    /**
     * 指定位数、日期补齐
     * 例如：ID.date(10, date, 10) 补齐为201403030000000010
     * @param id
     * @param date
     * @param length
     * @return
     * */
    public static String date(Serializable id, Date date, int length) {
        return date(id, date, FORMAT, length);
    }

    /**
     * 日期、日期格式补齐
     * 例如：ID.date(10, date, 10) 补齐为201403030000000010
     * @param id
     * @param date
     * @param format
     * @return
     * */
    public static String date(Serializable id, Date date, String format) {
        return date(id, date, format, LENGTH);
    }

    /**
     * 指定位数、日期和日期格式补齐
     * 例如：ID.date(10, 5, date, "yyyyMM") 补齐为20140300010
     * @param id
     * @param date
     * @param format
     * @param length
     * @return
     * */
    public static String date(Serializable id, Date date, String format, int length) {
        return gen(id, DateUtil.format(date, format), length);
    }
}
