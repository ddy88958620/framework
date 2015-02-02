package com.handu.apollo.cache;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by markerking on 14/8/13.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE, METHOD })
public @interface Cache {
    CacheType type() default CacheType.READ_WRITE;
    CacheScope scope() default CacheScope.SELF;
}
