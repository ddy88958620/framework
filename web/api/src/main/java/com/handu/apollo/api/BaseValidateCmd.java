package com.handu.apollo.api;

import com.handu.apollo.utils.exception.ParameterValidationException;

/**
 * Created by markerking on 14-4-8.
 */
public abstract class BaseValidateCmd extends BaseCmd {
    public abstract void validate() throws ParameterValidationException;
}
