package com.handu.apollo.core;

import com.handu.apollo.utils.exception.ApolloAuthenticationException;
import com.handu.apollo.utils.exception.ApolloRuntimeException;

import java.util.Map;

/**
 * Created by markerking on 14-4-15.
 */
public interface ApolloServerService {
    public boolean verifyRequest(Map<String, Object[]> requestParameters) throws ApolloRuntimeException;
    public String userLogin(String username, String password) throws ApolloAuthenticationException;
    public void userLogout(String sessionKey);
    public boolean verifyUser(String sessionKey);
}
