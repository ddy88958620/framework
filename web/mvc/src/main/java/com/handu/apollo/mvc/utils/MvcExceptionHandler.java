package com.handu.apollo.mvc.utils;

import com.google.common.collect.Maps;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.exception.ApiException;
import com.handu.apollo.utils.exception.ApolloAuthenticationException;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 异常捕获
 * Created by markerking on 14/8/13.
 */
@ControllerAdvice(annotations = RestController.class)
public class MvcExceptionHandler {

    private static final Log LOG = Log.getLog(MvcExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object handleException(Exception exception, HttpServletResponse response) {
        LOG.error(exception.getMessage(), exception);
        Map map = Maps.newHashMap();
        if (exception instanceof ApolloAuthenticationException) {
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            map.put("errorCode", HttpStatus.SC_UNAUTHORIZED);
        } else if (exception instanceof ApiException) {
            response.setStatus(432);
            map.put("errorCode", 432);
        } else {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            map.put("errorCode", HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
        map.put("errorText", exception.getMessage());

        return map;
    }
}