package com.handu.apollo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.handu.apollo.utils.exception.ApiException;
import com.handu.apollo.utils.exception.ApolloAuthenticationException;
import com.handu.apollo.utils.exception.ApolloRuntimeException;
import com.handu.apollo.utils.json.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by markerking on 14/10/14.
 */
public class SdkUtil {
    private HttpClient httpClient;
    private String baseUri;
    private ObjectMapper mapper = JsonUtil.getMapper();

    public SdkUtil(String baseUri) {
        httpClient = HttpClients.createDefault();
        this.baseUri = baseUri;
    }

    public Object request(HttpServletRequest request) {
        try {
            String method = request.getMethod();
            String uri = request.getRequestURI().replace(request.getContextPath() + StringPool.FORWARD_SLASH, StringPool.BLANK);
            String cookie = request.getHeader("Cookie");
            Map<String, String[]> params = request.getParameterMap();
            if (method.equalsIgnoreCase("POST")) {
                return this.post(uri, params, cookie);
            } else {
                return this.get(uri, params, cookie);
            }
        } catch (IOException e) {
            throw new ApolloRuntimeException("远程连接出现异常", e);
        }
    }

    private Object get(String command, Map<String, String[]> params, String cookie) throws IOException {
        RequestBuilder builder = RequestBuilder.get().setUri(this.baseUri + command);
        if (params != null) {
            for (String key : params.keySet()) {
                for (String value : params.get(key)) {
                    builder = builder.addParameter(key, value);
                }
            }
        }
        return request(builder.build(), cookie);
    }

    private Object post(String uri, Map<String, String[]> params, String cookie) throws IOException {
        HttpPost httpRequest = new HttpPost(this.baseUri + uri);
        List<NameValuePair> appParams = Lists.newArrayList();

        if (params != null) {
            for (String key : params.keySet()) {
                for (String value : params.get(key)) {
                    appParams.add(new BasicNameValuePair(key, value));
                }
            }
        }

        HttpEntity httpEntity = new UrlEncodedFormEntity(appParams, StringPool.UTF8);
        httpRequest.setEntity(httpEntity);

        return request(httpRequest, cookie);
    }

    private Object request(HttpUriRequest request, String cookie) throws IOException {
        request.setHeader("Cookie", cookie);
        HttpResponse response = httpClient.execute(request);
        String responseText = EntityUtils.toString(response.getEntity());
        if (StringUtils.isNotBlank(responseText)) {
            Object result = mapper.readValue(responseText, Object.class);
            if (result instanceof Map) {
                Map map = ((Map) result);
                if (map.get("errorCode") != null) {
                    if (map.get("errorCode").equals(HttpStatus.SC_UNAUTHORIZED)) {
                        throw new ApolloAuthenticationException((String) map.get("errorText"));
                    } else if (map.get("errorCode").equals(432)) {
                        throw new ApiException((String) map.get("errorText"));
                    }
                    throw new ApolloRuntimeException((String) map.get("errorText"));
                }
            }
            return result;
        } else {
            return null;
        }
    }
}
