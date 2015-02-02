package com.handu.apollo.core;

public enum ApiErrorCode {

    MALFORMED_PARAMETER_ERROR(430),
    PARAM_ERROR(431),
    UNSUPPORTED_ACTION_ERROR(432),
    API_LIMIT_EXCEED(429),

    INTERNAL_ERROR(530),
    ACCOUNT_ERROR(531),
    ACCOUNT_RESOURCE_LIMIT_ERROR(532),
    PARAMETER_VALIDATION_ERROR(535);

    private int httpCode;

    private ApiErrorCode(int httpStatusCode) {
        httpCode = httpStatusCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String toString() {
        return String.valueOf(this.httpCode);
    }


}