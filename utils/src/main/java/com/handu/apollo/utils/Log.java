package com.handu.apollo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by markerking on 14-4-22.
 */
public class Log {

    private Log() {}
    private Logger LOG;

    public void debug(String message, Throwable t) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message, t);
        }
    }

    public void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    public void debug(String message, Object... objects) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message, objects);
        }
    }

    public void info(String message) {
        if (LOG.isInfoEnabled()) {
            LOG.info(message);
        }
    }

    public void info(String message, Throwable t) {
        if (LOG.isInfoEnabled()) {
            LOG.info(message, t);
        }
    }

    public void info(String message, Object... objects) {
        if (LOG.isInfoEnabled()) {
            LOG.info(message, objects);
        }
    }

    public void trace(String message) {
        if (LOG.isTraceEnabled()) {
            LOG.trace(message);
        }
    }

    public void trace(String message, Throwable t) {
        if (LOG.isTraceEnabled()) {
            LOG.trace(message, t);
        }
    }

    public void trace(String message, Object... objects) {
        if (LOG.isTraceEnabled()) {
            LOG.trace(message, objects);
        }
    }

    public void warn(String message) {
        if (LOG.isWarnEnabled()) {
            LOG.warn(message);
        }
    }

    public void warn(String message, Throwable t) {
        if (LOG.isWarnEnabled()) {
            LOG.warn(message, t);
        }
    }

    public void warn(String message, Object... objects) {
        if (LOG.isWarnEnabled()) {
            LOG.warn(message, objects);
        }
    }

    public void error(String message) {
        if (LOG.isErrorEnabled()) {
            LOG.error(message);
        }
    }

    public void error(String message, Throwable t) {
        if (LOG.isErrorEnabled()) {
            LOG.error(message, t);
        }
    }

    public void error(String message, Object... objects) {
        if (LOG.isErrorEnabled()) {
            LOG.error(message, objects);
        }
    }

    public boolean isTraceEnabled() {
        return LOG.isTraceEnabled();
    }

    public boolean isDebugEnabled() {
        return LOG.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return LOG.isInfoEnabled();
    }

    public boolean isWarnEnabled() {
        return LOG.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return LOG.isErrorEnabled();
    }

    public static Log getLog(Class clazz) {
        return getLog(clazz.getName());
    }

    public static Log getLog(String name) {
        Log instance = new Log();
        instance.LOG = LoggerFactory.getLogger(name);
        return instance;
    }
}
