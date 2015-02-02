package com.handu.apollo.utils.exception;

import com.handu.apollo.utils.SerialVersionUID;

/**
 * Created by markerking on 14-4-3.
 */
public class ApolloRuntimeException extends RuntimeException {

    private static final long serialVersionUID = SerialVersionUID.ApolloRuntimeException;

    public ApolloRuntimeException() {
        super();
    }

    public ApolloRuntimeException(String message) {
        super(message);
    }

    public ApolloRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
