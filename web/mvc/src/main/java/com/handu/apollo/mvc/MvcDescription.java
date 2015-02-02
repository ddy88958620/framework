package com.handu.apollo.mvc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Created by markerking on 14/8/14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ METHOD })
public @interface MvcDescription {
    String value();
    String[] excludes() default {};
    String[] includes() default {};
    String extend() default "";
}
