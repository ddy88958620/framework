package com.handu.apollo.mvc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by markerking on 14/8/14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE })
public @interface MvcGroup {
    String value();
    String[] includes() default {};
}
