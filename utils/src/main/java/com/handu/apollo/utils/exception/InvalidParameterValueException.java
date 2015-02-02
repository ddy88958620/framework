package com.handu.apollo.utils.exception;

import com.handu.apollo.utils.SerialVersionUID;
import com.handu.apollo.utils.exception.ApolloRuntimeException;

public class InvalidParameterValueException extends ApolloRuntimeException {

    private static final long serialVersionUID = SerialVersionUID.InvalidParameterValueException;

    public InvalidParameterValueException(String message) {
        super(message);
    }

}