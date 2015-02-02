package com.handu.apollo.api.response;

/**
 * Created by markerking on 14-4-3.
 */
public class ExceptionResponse extends BaseResponse {

    private Integer errorCode;
    private String errorText;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
