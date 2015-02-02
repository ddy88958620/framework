package com.handu.apollo.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by markerking on 14/8/13.
 */
public final class ValidateUtil {

    private ValidateUtil() {}

    public static boolean equals(boolean boolean1, boolean boolean2) {
        return boolean1 == boolean2;
    }

    public static boolean equals(byte byte1, byte byte2) {
        return byte1 == byte2;
    }

    public static boolean equals(char char1, char char2) {
        return char1 == char2;
    }

    public static boolean equals(double double1, double double2) {
        return Double.compare(double1, double2) == 0;
    }

    public static boolean equals(float float1, float float2) {
        return Float.compare(float1, float2) == 0;
    }

    public static boolean equals(int int1, int int2) {
        return int1 == int2;
    }

    public static boolean equals(long long1, long long2) {
        return long1 == long2;
    }

    public static boolean equals(Object obj1, Object obj2) {
        return (obj1 == null) && (obj2 == null) || !((obj1 == null) || (obj2 == null)) && obj1.equals(obj2);
    }

    public static boolean equals(short short1, short short2) {
        return short1 == short2;
    }

    public static boolean isAddress(String address) {
        if (isNull(address)) {
            return false;
        }

        String[] tokens = address.split(StringPool.AT);

        if (tokens.length != 2) {
            return false;
        }

        for (String token : tokens) {
            for (char c : token.toCharArray()) {
                if (Character.isWhitespace(c)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isAscii(char c) {
        return ((int) c >= 32) && ((int) c <= 126);
    }

    /**
     * Returns <code>true</code> if c is a letter between a-z and A-Z.
     *
     * @return <code>true</code> if c is a letter between a-z and A-Z
     */
    public static boolean isChar(char c) {
        return ((int) c >= _CHAR_BEGIN) && ((int) c <= _CHAR_END);

    }

    /**
     * Returns <code>true</code> if s is a string of letters that are between
     * a-z and A-Z.
     *
     * @return <code>true</code> if s is a string of letters that are between
     *         a-z and A-Z
     */
    public static boolean isChar(String s) {
        if (isNull(s)) {
            return false;
        }

        for (char c : s.toCharArray()) {
            if (!isChar(c)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isDate(int month, int day, int year) {
        return isGregorianDate(month, day, year);
    }

    /**
     * Returns <code>true</code> if c is a digit between 0 and 9.
     *
     * @return <code>true</code> if c is a digit between 0 and 9
     */
    public static boolean isDigit(char c) {
        return ((int) c >= _DIGIT_BEGIN) && ((int) c <= _DIGIT_END);

    }

    /**
     * Returns <code>true</code> if s is a string of letters that are between 0
     * and 9.
     *
     * @return <code>true</code> if s is a string of letters that are between 0
     *         and 9
     */
    public static boolean isDigit(String s) {
        if (isNull(s)) {
            return false;
        }

        for (char c : s.toCharArray()) {
            if (!isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isDomain(String domainName) {

        // See RFC-1034 (section 3), RFC-1123 (section 2.1), and RFC-952
        // (section B. Lexical grammar)

        if (isNull(domainName)) {
            return false;
        }

        if (domainName.length() > 255) {
            return false;
        }

        String[] domainNameArray = domainName.split(StringPool.PERIOD);

        for (String domainNamePart : domainNameArray) {
            char[] domainNamePartCharArray = domainNamePart.toCharArray();

            for (int i = 0; i < domainNamePartCharArray.length; i++) {
                char c = domainNamePartCharArray[i];

                if ((i == 0) && (c == CharPool.DASH)) {
                    return false;
                } else if ((i == (domainNamePartCharArray.length - 1))
                        && (c == CharPool.DASH)) {

                    return false;
                } else if ((!isChar(c)) && (!isDigit(c))
                        && (c != CharPool.DASH)) {

                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isEmailAddress(String emailAddress) {
        return !isNull(emailAddress) && _emailer.matcher(emailAddress).matches();
    }

    public static boolean isEmailAddressSpecialChar(char c) {

        // LEP-1445

        for (int i = 0; i < _EMAIL_ADDRESS_SPECIAL_CHAR.length; i++) {
            if (c == _EMAIL_ADDRESS_SPECIAL_CHAR[i]) {
                return true;
            }
        }

        return false;
    }

    public static boolean isGregorianDate(int month, int day, int year) {
        if ((month < 0) || (month > 11)) {
            return false;
        }

        int[] months = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        if (month == 1) {
            int febMax = 28;

            if (((year % 4) == 0) && ((year % 100) != 0) || ((year % 400) == 0)) {

                febMax = 29;
            }

            if ((day < 1) || (day > febMax)) {
                return false;
            }
        } else if ((day < 1) || (day > months[month])) {
            return false;
        }

        return true;
    }

    public static boolean isHex(String s) {
        return !isNull(s);

    }

    public static boolean isHTML(String s) {
        return !isNull(s) && ((s.contains("<html>")) || (s.contains("<HTML>"))) && ((s.contains("</html>")) || (s.contains("</HTML>")));
    }

    public static boolean isIPAddress(String ipAddress) {
        Matcher matcher = _ipAddressPattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static boolean isJulianDate(int month, int day, int year) {
        if ((month < 0) || (month > 11)) {
            return false;
        }

        int[] months = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        if (month == 1) {
            int febMax = 28;

            if ((year % 4) == 0) {
                febMax = 29;
            }

            if ((day < 1) || (day > febMax)) {
                return false;
            }
        } else if ((day < 1) || (day > months[month])) {
            return false;
        }

        return true;
    }

    public static boolean isEnName(String name) {
        if (isNull(name)) {
            return false;
        }

        for (char c : name.trim().toCharArray()) {
            if (((!isChar(c)) && (!Character.isWhitespace(c)))
                    || (c == CharPool.COMMA)) {

                return false;
            }
        }

        return true;
    }

    public static boolean isNotNull(Long l) {
        return !isNull(l);
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isNotNull(Object[] array) {
        return !isNull(array);
    }

    public static boolean isNotNull(String s) {
        return !isNull(s);
    }

    public static boolean isNull(Long l) {
        return (l == null) || (l.longValue() == 0);
    }

    public static boolean isNotNull(List<?> list) {
        return !isNull(list);
    }

    /**
     *
     * @param list
     * @return
     * @author BuN_Ny
     */
    public static boolean isNull(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNull(Object obj) {
        if (obj instanceof Long) {
            return isNull((Long) obj);
        } else if (obj instanceof String) {
            return isNull((String) obj);
        } else {
            return obj == null;
        }
    }

    public static boolean isNull(Object[] array) {
        return (array == null) || (array.length == 0);
    }

    public static boolean isNull(String s) {
        if (s == null) {
            return true;
        }

        int counter = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == CharPool.SPACE) {
                continue;
            } else if (counter > 3) {
                return false;
            }

            if (counter == 0) {
                if (c != CharPool.LOWER_CASE_N) {
                    return false;
                }
            } else if (counter == 1) {
                if (c != CharPool.LOWER_CASE_U) {
                    return false;
                }
            } else if ((counter == 2) || (counter == 3)) {
                if (c != CharPool.LOWER_CASE_L) {
                    return false;
                }
            }

            counter++;
        }

        return (counter == 0) || (counter == 4);

    }

    public static boolean isNumber(String number) {
        if (isNull(number)) {
            return false;
        }

        for (char c : number.toCharArray()) {
            if (!isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isPassword(String password) {
        if (isNull(password)) {
            return false;
        }

        if (password.length() < 6) {
            return false;
        }

        for (char c : password.toCharArray()) {
            if (!isChar(c) && !isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isUrl(String url) {
        if (isNotNull(url)) {
            try {
                new URL(url);

                return true;
            } catch (MalformedURLException ignored) {
            }
        }

        return false;
    }

    public static boolean isVariableName(String variableName) {
        if (isNull(variableName)) {
            return false;
        }

        Matcher matcher = _variableNamePattern.matcher(variableName);

        return matcher.matches();
    }

    public static boolean isVariableTerm(String s) {
        return s.startsWith(_VARIABLE_TERM_BEGIN) && s.endsWith(_VARIABLE_TERM_END);
    }

    public static boolean isWhitespace(char c) {
        return ((int) c == 0) || Character.isWhitespace(c);
    }

    public static boolean isXml(String s) {
        return s.startsWith(_XML_BEGIN) || s.startsWith(_XML_EMPTY);
    }

    private static final int _CHAR_BEGIN = 65;

    private static final int _CHAR_END = 122;

    private static final int _DIGIT_BEGIN = 48;

    private static final int _DIGIT_END = 57;

    private static final char[] _EMAIL_ADDRESS_SPECIAL_CHAR = new char[] { '.',
            '!', '#', '$', '%', '&', '\'', '*', '+', '-', '/', '=', '?', '^',
            '_', '`', '{', '|', '}', '~' };

    private static final String _VARIABLE_TERM_BEGIN = "[$";

    private static final String _VARIABLE_TERM_END = "$]";

    private static final String _XML_BEGIN = "<?xml";

    private static final String _XML_EMPTY = "<root />";

    private static Pattern _ipAddressPattern = Pattern.compile("\\b"
            + "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\."
            + "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\."
            + "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\."
            + "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])" + "\\b");
    private static Pattern _variableNamePattern = Pattern
            .compile("[_a-zA-Z]+[_a-zA-Z0-9]*");
    private static Pattern _emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
}
