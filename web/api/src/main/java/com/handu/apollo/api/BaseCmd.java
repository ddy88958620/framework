package com.handu.apollo.api;

import com.google.common.collect.Maps;
import com.handu.apollo.api.response.ResponseObject;
import com.handu.apollo.core.ApiErrorCode;
import com.handu.apollo.utils.exception.ApiException;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.SerializeUtil;
import com.handu.apollo.utils.StringPool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by markerking on 14-4-1.
 */
public abstract class BaseCmd {
    private static final Log LOG = Log.getLog(BaseCmd.class);

    public enum CommandType {
        BOOLEAN, DATE, FLOAT, DOUBLE, INTEGER, SHORT, LIST, LONG, OBJECT, MAP, STRING
    }

    public enum HTTPMethod {
        GET, POST, PUT, DELETE
    }

    private HTTPMethod httpMethod;
    private Map<String, String> fullUrlParams;
    private ResponseObject responseObject = null;

    public abstract void execute() throws ApiException;

    public void configure() {
    }

    public HTTPMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String method) {
        if (method != null) {
            if (method.equalsIgnoreCase("GET")) {
                httpMethod = HTTPMethod.GET;
            } else if (method.equalsIgnoreCase("PUT")) {
                httpMethod = HTTPMethod.PUT;
            } else if (method.equalsIgnoreCase("POST")) {
                httpMethod = HTTPMethod.POST;
            } else if (method.equalsIgnoreCase("DELETE")) {
                httpMethod = HTTPMethod.DELETE;
            }
        } else {
            httpMethod = HTTPMethod.GET;
        }
    }

    public void setHttpMethod(HTTPMethod method) {
        httpMethod = method;
    }

    public Map<String, String> getFullUrlParams() {
        return fullUrlParams;
    }

    public void setFullUrlParams(Map<String, String> fullUrlParams) {
        this.fullUrlParams = fullUrlParams;
    }

    public ResponseObject getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(ResponseObject responseObject) {
        this.responseObject = responseObject;
    }

    public String getCommandName() {
        ApiCommand command = this.getClass().getAnnotation(ApiCommand.class);
        return command.name();
    }

    public String getGroupName() {
        ApiCommand command = this.getClass().getAnnotation(ApiCommand.class);
        return command.groupName();
    }

    public String getGroupCacheKey() {
        return getGroupName() + StringPool.COLON + StringPool.STAR;
    }

    public String getCacheKey() {
        return getGroupName() + StringPool.COLON + new String(SerializeUtil.serialize(this.fullUrlParams));
    }

    public Map<String, Object> unpackParams(Map<String, String> params) {
        final String PARSE_ERROR = "无法解码参数[%s]; 如果要定义一个对象数组，使用 parameter[index].field=XXX，例如：userOderList[0].orderId=xxx";
        Map<String, Object> lowercaseParams = Maps.newHashMap();
        for (String key : params.keySet()) {
            int arrayStartIndex = key.indexOf('[');
            int arrayStartLastIndex = key.lastIndexOf('[');
            if (arrayStartIndex != arrayStartLastIndex) {
                throw new ApiException(ApiErrorCode.MALFORMED_PARAMETER_ERROR, String.format(PARSE_ERROR, key));
            }

            if (arrayStartIndex > 0) {
                int arrayEndIndex = key.indexOf(']');
                int arrayEndLastIndex = key.lastIndexOf(']');
                if ((arrayEndIndex < arrayStartIndex) || (arrayEndIndex != arrayEndLastIndex)) {
                    // 这是什么格式的参数?重新搞
                    throw new ApiException(ApiErrorCode.MALFORMED_PARAMETER_ERROR, String.format(PARSE_ERROR, key));
                }

                // 现在我们有了一个对象数组，检查字段名称是否正确
                int fieldIndex = key.indexOf('.');
                String fieldName;
                if (fieldIndex < arrayEndIndex) {
                    throw new ApiException(ApiErrorCode.MALFORMED_PARAMETER_ERROR, String.format(PARSE_ERROR, key));
                } else {
                    fieldName = key.substring(fieldIndex + 1);
                }

                // 将第一个[之前的字符串解析成参数名
                String paramName = key.substring(0, arrayStartIndex);
                //不再转换为小写 paramName = paramName.toLowerCase();

                Map<Integer, Map> mapArray = null;
                Map<String, Object> mapValue = null;
                String indexStr = key.substring(arrayStartIndex + 1, arrayEndIndex);
                int index = 0;
                boolean parsedIndex = false;
                try {
                    index = Integer.parseInt(indexStr);
                    parsedIndex = true;
                } catch (NumberFormatException nfe) {
                    LOG.warn("收到错误的参数[" + key + "]，无法解析为对象数组，返回错误");
                }

                if (!parsedIndex) {
                    throw new ApiException(ApiErrorCode.MALFORMED_PARAMETER_ERROR, String.format(PARSE_ERROR, key));
                }

                Object value = lowercaseParams.get(paramName);
                if (value == null) {
                    // 现在，假设对象数组有子字段
                    mapArray = Maps.newHashMap();
                    mapValue = Maps.newHashMap();
                    mapArray.put(Integer.valueOf(index), mapValue);
                } else if (value instanceof Map) {
                    mapArray = (HashMap) value;
                    mapValue = mapArray.get(Integer.valueOf(index));
                    if (mapValue == null) {
                        mapValue = Maps.newHashMap();
                        mapArray.put(Integer.valueOf(index), mapValue);
                    }
                }

                // 我们已经准备好为某个特定字段的值存储到map
                mapValue.put(fieldName, params.get(key));

                lowercaseParams.put(paramName, mapArray);
            } else {
                lowercaseParams.put(key, params.get(key));
            }
        }
        return lowercaseParams;
    }
}
