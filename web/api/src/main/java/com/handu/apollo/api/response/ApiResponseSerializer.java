package com.handu.apollo.api.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.exception.ApolloRuntimeException;
import com.handu.apollo.utils.json.JsonUtil;

/**
 * Created by markerking on 14-4-3.
 */
public class ApiResponseSerializer {
    private static final Log LOG = Log.getLog(ApiResponseSerializer.class);
    private static final ObjectMapper mapper = JsonUtil.getMapper();

    public static String toSerializedJSON(ResponseObject result) {
        LOG.trace("===序列化响应===");
        try {
            String json;
            if (result instanceof ListResponse) {
                json = mapper.writeValueAsString(result);
            } else if (result instanceof ArrayResponse) {
                json =  mapper.writeValueAsString(((ArrayResponse) result).getResponses());
            } else if (result instanceof JsonResponse) {
                json = ((JsonResponse) result).getJson();
            } else {
                String jsonStr = mapper.writeValueAsString(result);
                if (jsonStr != null && !"{}".equals(jsonStr)) {
                    json = jsonStr;
                } else {
                    json = "null";
                }
            }
            LOG.trace(json);
            return json;
        } catch (JsonProcessingException e) {
            throw new ApolloRuntimeException("序列化出现异常：", e);
        }
    }
}
