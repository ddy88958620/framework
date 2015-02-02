package com.handu.apollo.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by markerking on 14-5-19.
 */
public final class CodeUtil {

    private CodeUtil() {}

    /**
     * 部门编号的每一分段长度，如为3则code将类似123-456
     */
    public static final int CODE_LENGTH = 4;

    /**
     * 获得0补齐
     * @param length
     * @return
     */
    private static String getZeroCode(int length) {
        String fix = "";
        for(;length > 0; length--) {
            fix += "0";
        }
        return fix;
    }

    public static String getFirstCode(String superCode) {
        return getFirstCode(superCode, CODE_LENGTH);
    }

    /**
     * 根据父级code获得第一个code
     * @param superCode
     * @param length
     * @return
     */
    public static String getFirstCode(String superCode, int length) {
        if(StringUtils.isNotBlank(superCode)) {
            return getNextCode(superCode + "-" + getZeroCode(length), length);
        } else {
            return getNextCode(getZeroCode(length), length);
        }
    }

    public static String getNextCode(String code) {
        return getNextCode(code, CODE_LENGTH);
    }

    /**
     * 获得下一个code
     * @param code
     * @param length
     * @return
     */
    public static String getNextCode(String code, int length) {
        StringBuilder sb = new StringBuilder();
        String[] codeSplit = code.split("-");
        String lastCode = codeSplit[codeSplit.length-1];
        Integer lastNumber = Integer.parseInt(lastCode) + 1;
        String nextLastCode = getZeroCode(length) + lastNumber;
        nextLastCode = nextLastCode.substring(nextLastCode.length() - length);
        for(int i = 0, arrayLength = (codeSplit.length - 1); i < arrayLength; i++) {
            sb.append(codeSplit[i]);
            sb.append("-");
        }
        sb.append(nextLastCode);
        return sb.toString();
    }

    public static String fixCode(String code) {
        return fixCode(code, CODE_LENGTH);
    }

    /**
     * 补零方法
     * @param code 传入已有值
     * @param length 传入总长度
     * @return
     */
    public static String fixCode(String code, int length) {
        String s = getZeroCode(length) + code;
        return s.substring(code.length(), s.length());
    }
}
