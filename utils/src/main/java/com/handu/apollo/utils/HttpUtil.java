package com.handu.apollo.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by markerking on 14-7-9.
 */
public class HttpUtil {

    private String uri;
    private Map<String, String> params;

    public HttpUtil() {
    }

    public HttpUtil(String uri, Map<String, String> params) {
        this.uri = uri;
        this.params = params;
    }

    public String get() throws IOException {
        RequestBuilder builder = RequestBuilder.get().setUri(this.uri).setConfig(this.getDefaultConfig());
        if (this.params != null) {
            for (String key : this.params.keySet()) {
                builder = builder.addParameter(key, this.params.get(key));
            }
        }
        return request(builder.build());
    }

    public String get(String uri, Map<String, String> params) throws IOException {
        RequestBuilder builder = RequestBuilder.get().setUri(uri).setConfig(this.getDefaultConfig());
        if (params != null) {
            for (String key : params.keySet()) {
                builder = builder.addParameter(key, params.get(key));
            }
        }
        return request(builder.build());
    }

    public String post() throws IOException {
        HttpPost httpRequest = new HttpPost(this.uri);
        List<NameValuePair> appParams = Lists.newArrayList();

        if (this.params != null) {
            for (String key : this.params.keySet()) {
                appParams.add(new BasicNameValuePair(key, this.params.get(key)));
            }
        }

        HttpEntity httpEntity = new UrlEncodedFormEntity(appParams, StringPool.UTF8);
        httpRequest.setEntity(httpEntity);
        httpRequest.setConfig(this.getDefaultConfig());

        return request(httpRequest);
    }

    public String post(String uri, Map<String, String> params) throws IOException {
        HttpPost httpRequest = new HttpPost(uri);
        List<NameValuePair> appParams = Lists.newArrayList();

        if (params != null) {
            for (String key : params.keySet()) {
                appParams.add(new BasicNameValuePair(key, params.get(key)));
            }
        }

        HttpEntity httpEntity = new UrlEncodedFormEntity(appParams, StringPool.UTF8);
        httpRequest.setEntity(httpEntity);
        httpRequest.setConfig(this.getDefaultConfig());

        return request(httpRequest);
    }

    private String request(HttpUriRequest request) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createMinimal();
        CloseableHttpResponse response = httpClient.execute(request);
        String result = EntityUtils.toString(response.getEntity());
        response.close();
        httpClient.close();
        return result;
    }

    private RequestConfig getDefaultConfig() {
        return RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
    }

    public static String getAppUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return StringPool.BLANK;
        }
        if (url.lastIndexOf(StringPool.FORWARD_SLASH) != url.length() - 1) {
            return url + StringPool.FORWARD_SLASH;
        }
        return url;
    }
}
