package com.handu.apollo.utils.exception;

import com.handu.apollo.core.ApiErrorCode;

/**
 * Created by markerking on 14-4-3.
 */
public class ApiException extends ApolloRuntimeException {
    private ApiErrorCode errorCode;
    private String message;

    public ApiException() {
        errorCode = ApiErrorCode.INTERNAL_ERROR;
        message = null;
    }

    public ApiException(String message) {
        this.errorCode = ApiErrorCode.INTERNAL_ERROR;
        this.message = message;
    }

    public ApiException(ApiErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ApiException(ApiErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.message = message;
    }

    public ApiErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ApiErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
