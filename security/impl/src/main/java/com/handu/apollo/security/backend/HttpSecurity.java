package com.handu.apollo.security.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.handu.apollo.security.domain.SecurityUser;
import com.handu.apollo.utils.HttpUtil;
import com.handu.apollo.utils.exception.ApolloRuntimeException;
import com.handu.apollo.utils.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by markerking on 14/10/22.
 */
public class HttpSecurity implements SecurityBackend {

    @Autowired
    Environment env;

    private HttpUtil http = new HttpUtil();
    private ObjectMapper mapper = JsonUtil.getMapper();
    private String mdmApiUrl;

    @PostConstruct
    public void init() {
        mdmApiUrl = HttpUtil.getAppUrl(env.getProperty("mdm.url")) + "api";
    }

    @Override
    public Long getUserId(String username) {
        Map<String, String> params = Maps.newHashMap();
        params.put("username", username);
        params.put("command", SecurityQuery.userPk);
        try {
            String response = http.post(mdmApiUrl, params);
            return mapper.readValue(response, Long.class);
        } catch (IOException e) {
            throw new ApolloRuntimeException("远程连接无效", e);
        }
    }

    @Override
    public SecurityUser getUser(String username) {
        Map<String, String> params = Maps.newHashMap();
        params.put("username", username);
        params.put("command", SecurityQuery.user);
        try {
            String response = http.post(mdmApiUrl, params);
            return mapper.readValue(response, SecurityUser.class);
        } catch (IOException e) {
            throw new ApolloRuntimeException("远程连接无效", e);
        }
    }

    @Override
    public Set<String> getUserRoles(Long userId) {
        Map<String, String> params = Maps.newHashMap();
        params.put("id", userId.toString());
        params.put("command", SecurityQuery.roles);
        try {
            String response = http.post(mdmApiUrl, params);
            return mapper.readValue(response, HashSet.class);
        } catch (IOException e) {
            throw new ApolloRuntimeException("远程连接无效", e);
        }
    }

    @Override
    public Set<String> getUserPermissions(Long userId) {
        Map<String, String> params = Maps.newHashMap();
        params.put("id", userId.toString());
        params.put("command", SecurityQuery.permissions);
        try {
            String response = http.post(mdmApiUrl, params);
            return mapper.readValue(response, HashSet.class);
        } catch (IOException e) {
            throw new ApolloRuntimeException("远程连接无效", e);
        }
    }

    @Override
    public Set<String> getUserPermissionsByMisid(Long userId,String misId) {
        Map<String, String> params = Maps.newHashMap();
        params.put("id", userId.toString());
        params.put("misId",misId);
        params.put("command", SecurityQuery.permissions);
        try {
            String response = http.post(mdmApiUrl, params);
            return mapper.readValue(response, HashSet.class);
        } catch (IOException e) {
            throw new ApolloRuntimeException("远程连接无效", e);
        }
    }
}
