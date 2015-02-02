package com.handu.apollo.utils.exception;

import com.handu.apollo.utils.SerialVersionUID;
import com.handu.apollo.utils.exception.ApolloRuntimeException;

public class PermissionDeniedException extends ApolloRuntimeException {

    private static final long serialVersionUID = SerialVersionUID.PermissionDeniedException;

    public PermissionDeniedException(String message) {
        super(message);
    }

    public PermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    protected PermissionDeniedException() {
        super();
    }
}