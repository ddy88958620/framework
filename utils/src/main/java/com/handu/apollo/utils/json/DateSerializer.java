package com.handu.apollo.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.handu.apollo.utils.DateUtil;
import com.handu.apollo.utils.StringPool;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by markerking on 14-4-16.
 */
public class DateSerializer extends DateTimeSerializerBase<Date> {

    public DateSerializer() {
        this(false, null);
    }

    public DateSerializer(boolean useTimestamp, DateFormat customFormat) {
        super(Date.class, useTimestamp, customFormat);
    }

    @Override
    public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
        if (timestamp != null && timestamp) {
            return new DateSerializer(true, null);
        }
        return new DateSerializer(false, customFormat);
    }

    @Override
    protected long _timestamp(Date value) {
        return (value == null) ? 0L : value.getTime();
    }

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (_useTimestamp) {
            jgen.writeNumber(_timestamp(value));
        } else if (_customFormat != null) {
            _customFormat.setTimeZone(TimeZone.getTimeZone(StringPool.GMT8));
            synchronized (_customFormat) {
                jgen.writeString(_customFormat.format(value));
            }
        } else {
            jgen.writeString(DateUtil.format(value, DateUtil.YMDHM));
        }
    }
}
