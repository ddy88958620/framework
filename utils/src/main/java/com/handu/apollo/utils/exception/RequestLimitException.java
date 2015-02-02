package com.handu.apollo.utils.exception;

import com.handu.apollo.utils.SerialVersionUID;

public class RequestLimitException extends PermissionDeniedException {

    private static final long serialVersionUID = SerialVersionUID.RequestLimitException;

    protected RequestLimitException() {
        super();
    }

    public RequestLimitException(String msg) {
        super(msg);
    }

    public RequestLimitException(String msg, Throwable cause) {
        super(msg, cause);
    }

}