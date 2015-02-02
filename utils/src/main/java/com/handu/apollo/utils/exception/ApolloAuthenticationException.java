package com.handu.apollo.utils.exception;

import com.handu.apollo.utils.SerialVersionUID;
import com.handu.apollo.utils.exception.ApolloRuntimeException;

/**
 * Created by markerking on 14-4-3.
 */
public class ApolloAuthenticationException extends ApolloRuntimeException {

    private static final long serialVersionUID = SerialVersionUID.ApolloAuthenticationException;

    public ApolloAuthenticationException(String message) {
        super(message);
    }

    public ApolloAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
