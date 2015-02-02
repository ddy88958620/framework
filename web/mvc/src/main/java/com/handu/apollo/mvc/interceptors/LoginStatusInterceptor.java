package com.handu.apollo.mvc.interceptors;

import com.google.common.collect.Maps;
import com.handu.apollo.core.ApiConstants;
import com.handu.apollo.mvc.MvcServerService;
import com.handu.apollo.mvc.utils.ParamUtil;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class LoginStatusInterceptor extends HandlerInterceptorAdapter {

    private static final Log LOG = Log.getLog(LoginStatusInterceptor.class);

    @Autowired
    private MvcServerService _mvcServerService;

    private Long misId;

    public Long getMisId() {
        return misId;
    }

    public void setMisId(Long misId) {
        this.misId = misId;
    }

    public LoginStatusInterceptor() {
        // TODO Auto-generated constructor stub
    }

    /**
     * 在业务处理器处理请求之前被调用
     * 如果返回false
     * 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * <p/>
     * 如果返回true
     * 执行下一个拦截器,直到所有的拦截器都执行完毕
     * 再执行被拦截的Controller
     * 然后进入拦截器链,
     * 从最后一个拦截器往回执行所有的postHandle()
     * 接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI().replace(request.getContextPath(), StringPool.BLANK);

        String cmdStr = uri.substring(uri.lastIndexOf("/") + 1);
        if (cmdStr.equals("login") || cmdStr.equals("logout")) {
            return true;
        }

        String sessionKey = ParamUtil.getCookie(request, ApiConstants.SESSION_KEY);
        Map<String, Object[]> map = Maps.newHashMap();
        map.put(ApiConstants.SESSION_KEY, new String[]{sessionKey});
        map.put("commandName", new String[]{getCmd(cmdStr)});

        return _mvcServerService.verifyRequest(map);
    }

    //在业务处理器处理请求执行完成后,生成视图之前执行的动作
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * 在DispatcherServlet完全处理完请求后被调用
     * <p/>
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub
    }

    public String getCmd(String cmdStr){
        if(getMisId()==null)
            return cmdStr;
        return getMisId() + StringPool.COLON + cmdStr;
    }
}
