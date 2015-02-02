package com.handu.apollo.utils.exception;

import com.handu.apollo.utils.SerialVersionUID;
import com.handu.apollo.utils.exception.ApolloRuntimeException;

/**
 * Created by markerking on 14-4-8.
 */
public class AccountLimitException extends ApolloRuntimeException {

    private static final long serialVersionUID = SerialVersionUID.AccountLimitException;

    protected AccountLimitException() {
        super();
    }

    public AccountLimitException(String msg) {
        super(msg);
    }

    public AccountLimitException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
