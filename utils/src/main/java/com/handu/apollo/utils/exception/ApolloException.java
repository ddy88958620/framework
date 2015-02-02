package com.handu.apollo.utils.exception;

/**
 * Created by markerking on 14-4-3.
 */
public class ApolloException extends Exception {

    public ApolloException() {
        super();
    }

    public ApolloException(String message) {
        super(message);
    }

    public ApolloException(String message, Throwable cause) {
        super(message, cause);
    }
}
