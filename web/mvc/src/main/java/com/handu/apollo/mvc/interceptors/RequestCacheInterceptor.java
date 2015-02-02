package com.handu.apollo.mvc.interceptors;

import com.handu.apollo.cache.Cache;
import com.handu.apollo.cache.CacheScope;
import com.handu.apollo.cache.CacheType;
import com.handu.apollo.cache.RedisDao;
import com.handu.apollo.mvc.MvcGroup;
import com.handu.apollo.mvc.utils.ResponseWrapper;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.StringPool;
import com.handu.apollo.utils.exception.ApolloRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.TreeMap;

/**
 * Created by markerking on 14/8/18.
 */
public class RequestCacheInterceptor extends HandlerInterceptorAdapter {

    private static final Log LOG = Log.getLog(RequestCacheInterceptor.class);

    @Autowired
    private RedisDao cache;
    @Autowired
    private Environment env;
    /**
     * This implementation always returns {@code true}.
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String json = null;
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Cache cacheAnnotation = handlerMethod.getMethod().getAnnotation(Cache.class);

            if (cacheAnnotation != null) {
                String cacheKey = getCacheKey(handlerMethod, request);
                if (cacheAnnotation.type() == CacheType.READ || cacheAnnotation.type() == CacheType.READ_WRITE) {
                    json = cache.valueGet(cacheKey);
                    if (json != null) {
                        PrintWriter writer = response.getWriter();
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType("text/javascript; charset=UTF-8");
                        writer.write(json);
                    }
                } else {
                    LOG.debug(handlerMethod.getMethod().getName() + " 未开启读缓存");
                }
            }
        }
        return json == null;
    }

    /**
     * This implementation is empty.
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Cache cacheAnnotation = handlerMethod.getMethod().getAnnotation(Cache.class);
            if (cacheAnnotation != null) {
                String cacheKey = getCacheKey(handlerMethod, request);
                if (cacheAnnotation.type() == CacheType.WRITE || cacheAnnotation.type() == CacheType.READ_WRITE) {
                    if (cacheAnnotation.scope() == CacheScope.GROUP) {
                        LOG.debug(handlerMethod.getMethod().getName() + " 删除组级缓存");
                        cache.valueDelete(getGroupCacheKey(handlerMethod) + StringPool.STAR);
                    }
                    LOG.debug(handlerMethod.getMethod().getName() + " 数据回写入缓存");
                    ResponseWrapper responseWrapper = new ResponseWrapper(response);
                    cache.valueSet(cacheKey, responseWrapper.getContent());
                } else if (cacheAnnotation.type() == CacheType.READ) {
                    if (cache.valueGet(cacheKey) == null) {
                        LOG.debug(handlerMethod.getMethod().getName() + " 未命中缓存，数据回写入缓存");
                        ResponseWrapper responseWrapper = new ResponseWrapper(response);
                        cache.valueSet(cacheKey, responseWrapper.getContent());
                    }
                } else {
                    LOG.debug(handlerMethod.getMethod().getName() + " 未识别缓存配置");
                }
            }
        }
    }

    private String getGroupCacheKey(HandlerMethod handlerMethod) {
        MvcGroup group = handlerMethod.getBean().getClass().getAnnotation(MvcGroup.class);
        if (group != null) {
            String keyPrefix = env.getProperty("mdm.cache.keyPrefix");
            String groupName = handlerMethod.getBean().getClass().getSimpleName();
            return keyPrefix + groupName + StringPool.COLON;
        } else {
            throw new ApolloRuntimeException(String.format("[%s]类缺少注解MvcGroup", handlerMethod.getBean().getClass().getName()));
        }
    }

    private String getCacheKey(HandlerMethod handlerMethod, HttpServletRequest request) {
        RequestMapping requestMapping = handlerMethod.getMethodAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            String cmd = requestMapping.value()[0];
            TreeMap params = (TreeMap) request.getParameterMap();
            return getGroupCacheKey(handlerMethod) + cmd + StringPool.COLON + params.toString();
        } else {
            throw new ApolloRuntimeException(String.format("[%s]缺少注解RequestMapping", handlerMethod.getMethod().getName()));
        }
    }
}
