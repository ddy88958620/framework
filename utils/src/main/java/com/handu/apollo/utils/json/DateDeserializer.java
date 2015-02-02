package com.handu.apollo.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.handu.apollo.utils.DateUtil;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.StringPool;
import com.handu.apollo.utils.exception.ApolloRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

public final class DateDeserializer extends DateDeserializers.DateDeserializer {

    private static final Log LOG = Log.getLog(DateDeserializer.class);

    public DateDeserializer() { super(); }

    public DateDeserializer(DateDeserializer base, DateFormat df, String formatString) {
        super(base, df, formatString);
    }

    @Override
    protected DateDeserializer withDateFormat(DateFormat df, String formatString) {
        return new DateDeserializer(this, df, formatString);
    }

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        if (StringUtils.isEmpty(jp.getText())) {
            return getEmptyValue();
        }
        if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
            String str = jp.getText().trim();
            if (str.length() == 0) {
                return getEmptyValue();
            }
            if (_customFormat != null) {
                _customFormat.setTimeZone(TimeZone.getTimeZone(StringPool.GMT8));
                synchronized (_customFormat) {
                    try {
                        return _customFormat.parse(str);
                    } catch (ParseException e) {
                        throw new IllegalArgumentException("Failed to parse Date value '" + str
                                + "' (format: \"" + _formatString + "\"): " + e.getMessage());
                    }
                }
            }
        }
        return _parseDate(jp, ctxt);
    }

    @Override
    protected Date _parseDate(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String date = jp.getText().trim();
        try {
            return DateUtil.parse(date);
        } catch (ApolloRuntimeException e) {
            LOG.warn("反序列化日期时出现了错误：" + date);
            throw new IOException(date, e);
        }
    }
}