package com.handu.apollo.utils.exception;

import com.handu.apollo.utils.SerialVersionUID;
import com.handu.apollo.utils.exception.ApolloException;

/**
 * Created by markerking on 14-4-8.
 */
public class ParameterValidationException extends ApolloException {

    private static final long serialVersionUID = SerialVersionUID.ParameterValidationException;

    public ParameterValidationException(String message) {
        super(message);

    }

}
