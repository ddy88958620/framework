package com.handu.apollo.security.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.handu.apollo.utils.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by liuhongxu on 2014/12/3.
 */
public class CacheObject<V> implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(CacheObject.class);

    private static final String CHARSET_ENCODE = "UTF-8";
    private Class clazz;
    private String json;
    private String type;//文本格式类型,用于扩展

    public CacheObject(V obj)  {
        this.clazz = obj.getClass();
        try {
            this.json = JsonUtil.getMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("序列化对象[{}]异常", obj, e);
            throw new RuntimeException(e);
        }
    }

    public static CacheObject getCacheObject(byte[] bytes) {
        try {
            return JsonUtil.getMapper().readValue(bytes,CacheObject.class);
        } catch (IOException e) {
            try {
                logger.error("反序列化数据[{}]异常",bytes!=null?new String(bytes,CHARSET_ENCODE):"null",e);
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e);
        }
    }

    public CacheObject(){}

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public V readValue() {
        try {
            return (V)JsonUtil.getMapper().readValue(this.json,this.clazz);
        } catch (IOException e) {
            logger.error("反序列化{}数据[{}]异常",clazz,json,e);
            throw new RuntimeException(e);
        }
    }

    public byte[] writeValue(){
        try {
            return JsonUtil.getMapper().writeValueAsString(this).getBytes(CHARSET_ENCODE);
        } catch (JsonProcessingException e) {
            logger.error("序列化{}对象异常",clazz,e);
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
