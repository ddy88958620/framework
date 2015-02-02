package com.handu.apollo.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by markerking on 14-4-3.
 */
public final class DateUtil {

    private static final Log LOG = Log.getLog(DateUtil.class);

    //static method only
    private DateUtil() {
    }

    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String YMDHM = "yyyy-MM-dd HH:mm";
    public static final String YMDH = "yyyy-MM-dd HH";
    public static final String YMD = "yyyy-MM-dd";
    public static final String CH_YMDHMS = "yyyy年MM月dd日 HH:mm:ss";
    public static final String CH_YMDHM = "yyyy年MM月dd日 HH:mm";
    public static final String CH_YMDH = "yyyy年MM月dd日 HH";
    public static final String CH_YMD = "yyyy年MM月dd日";

    //thread unsafe
    private static ThreadLocal<SimpleDateFormat> DF_TL = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat(YMDHMS, Locale.ENGLISH);
        }
    };

    /**
     * 根据指定 format 格式化当前日期
     *
     * @param format
     * @return
     */
    public static String get(String format) {
        return format(format);
    }

    /**
     * 默认格式化日期
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, YMDHMS);
    }

    /**
     * 根据指定 format 格式化当前日期
     *
     * @param format
     * @return
     */
    public static String format(String format) {
        return format(new Date(), format);
    }

    /**
     * 根据指定 format 格式化指定的 date 日期
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        if (date == null) {
            return StringPool.BLANK;
        }
        final SimpleDateFormat formater = DF_TL.get();
        formater.setLenient(false);
        formater.applyPattern(format);
        return formater.format(date);
    }

    /**
     * 根据指定字符串返回日期类型
     *
     * @param string
     * @return
     */
    public static Date parse(String string) {
        try {
            return DateUtils.parseDateStrictly(
                    string,
                    Locale.CHINA,
                    YMDHMS,
                    CH_YMDHMS,
                    YMDHM,
                    CH_YMDHM,
                    YMDH,
                    CH_YMDH,
                    YMD,
                    CH_YMD);
        } catch (ParseException e) {
            LOG.warn("解析日期时出现了错误：" + string);
            return null;
        }
    }

    /**
     * 根据指定字符串和格式返回日期类型
     *
     * @param string
     * @param format
     * @return
     */
    public static Date parse(String string, String format) {
        try {
            final SimpleDateFormat formater = DF_TL.get();
            formater.setLenient(false);
            formater.applyPattern(format);
            return formater.parse(string);
        } catch (ParseException e) {
            LOG.warn("解析日期时出现了错误：" + string);
            return null;
        }
    }


    /**
     * 根据字符串类型的UNIX时间戳转换返回日期类型
     *
     * @param string
     * @return
     */
    public static Date timestamp2Date(String string) {
        return new Date(Long.parseLong(string) * 1000);
    }
}
