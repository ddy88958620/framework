package com.handu.apollo.mvc.interceptors;

import com.handu.apollo.utils.StringPool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by markerking on 14/9/2.
 */
public class JsonpInterceptor extends HandlerInterceptorAdapter {

    private static final String CALLBACK = "callback";

    /**
     * 登录过拦截器通过之后，判断是否是JSONP，并增加回调
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String callback = request.getParameter(CALLBACK);
        if (StringUtils.isNotBlank(callback)) {
            response.getOutputStream().print(callback + StringPool.OPEN_PARENTHESIS);
        }
        return super.preHandle(request, response, handler);
    }

    /**
     * 封闭JSONP回调
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String callback = request.getParameter(CALLBACK);
        if (StringUtils.isNotBlank(callback)) {
            response.getOutputStream().print(StringPool.CLOSE_PARENTHESIS + StringPool.SEMICOLON);
        }
    }
}
