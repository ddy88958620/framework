package com.handu.apollo.mvc.utils;

import com.handu.apollo.utils.DateUtil;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * Created by wangfei on 2014/8/21.
 */
public class ApolloDateEditor extends PropertyEditorSupport {

    private final boolean allowEmpty;

    private final int exactDateLength;


    /**
     * Create a new CustomDateEditor instance, using the given DateFormat
     * for parsing and rendering.
     * <p>The "allowEmpty" parameter states if an empty String should
     * be allowed for parsing, i.e. get interpreted as null value.
     * Otherwise, an IllegalArgumentException gets thrown in that case.
     * @param allowEmpty if empty strings should be allowed
     */
    public ApolloDateEditor(boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
        this.exactDateLength = -1;
    }

    /**
     * Create a new CustomDateEditor instance, using the given DateFormat
     * for parsing and rendering.
     * <p>The "allowEmpty" parameter states if an empty String should
     * be allowed for parsing, i.e. get interpreted as null value.
     * Otherwise, an IllegalArgumentException gets thrown in that case.
     * <p>The "exactDateLength" parameter states that IllegalArgumentException gets
     * thrown if the String does not exactly match the length specified. This is useful
     * because SimpleDateFormat does not enforce strict parsing of the year part,
     * not even with {@code setLenient(false)}. Without an "exactDateLength"
     * specified, the "01/01/05" would get parsed to "01/01/0005". However, even
     * with an "exactDateLength" specified, prepended zeros in the day or month
     * part may still allow for a shorter year part, so consider this as just
     * one more assertion that gets you closer to the intended date format.
     * @param allowEmpty if empty strings should be allowed
     * @param exactDateLength the exact expected length of the date String
     */
    public ApolloDateEditor(boolean allowEmpty, int exactDateLength) {
        this.allowEmpty = allowEmpty;
        this.exactDateLength = exactDateLength;
    }


    /**
     * Parse the Date from the given text, using the specified DateFormat.
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        }
        else if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
            throw new IllegalArgumentException(
                    "Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
        }
        else {
            try {
                setValue(DateUtil.parse(text));
            }catch (Exception ex) {
                throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
            }
        }
    }

    /**
     * Format the Date as String, using the specified DateFormat.
     */
    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return (value != null ? DateUtil.format(value) : "");
    }
}
