package com.handu.apollo.api;

import com.handu.apollo.core.ApolloServerService;
import com.handu.apollo.utils.exception.ApolloRuntimeException;

import java.util.Map;

/**
 * Created by markerking on 14/8/15.
 */
public interface ApiServerService extends ApolloServerService {
    public String handleRequest(Map params, StringBuffer auditTrailSb) throws ApolloRuntimeException;
    public String handleFileRequest(Map params, StringBuffer auditTrailSb, Map filenMap) throws ApolloRuntimeException;
}
