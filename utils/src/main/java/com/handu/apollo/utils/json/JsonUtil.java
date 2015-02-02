package com.handu.apollo.utils.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.Date;

/**
 * Created by markerking on 14-4-3.
 */
public final class JsonUtil {
    private static final ObjectMapper _mapper;

    static {
        _mapper = new ObjectMapper();

        // 注册日期序列化和反序列化
        SimpleModule newModule = new SimpleModule("DataModule", PackageVersion.VERSION);
        DateDeserializer dateDeserializer = new DateDeserializer();
        DateSerializer dateSerializer = new DateSerializer();

        newModule.addDeserializer(Date.class, dateDeserializer);
        newModule.addSerializer(Date.class, dateSerializer);
        _mapper.registerModule(newModule);

        // 禁用所有自动发现，所有取值直接取属性值，而不通过getter方式取
        // _mapper.setVisibilityChecker(_mapper.getVisibilityChecker().with(JsonAutoDetect.Visibility.NONE));
    }

    private JsonUtil() {}

    public static ObjectMapper getMapper() {
        return _mapper;
    }
}