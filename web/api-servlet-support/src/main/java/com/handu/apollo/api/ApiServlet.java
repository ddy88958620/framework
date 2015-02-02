package com.handu.apollo.api;

import com.google.common.collect.Maps;
import com.handu.apollo.core.ApiConstants;
import com.handu.apollo.core.ApiErrorCode;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.StringPool;
import com.handu.apollo.utils.exception.ApiException;
import com.handu.apollo.utils.exception.ApolloAuthenticationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * Created by markerking on 14-4-3.
 */
@Component
public class ApiServlet extends HttpServlet {

    private static final Log LOG = Log.getLog(ApiServlet.class.getName());
    private static final Log ACCESS_LOG = Log.getLog(ApiServlet.class);

    public static final String MULTIPART = "multipart/";
    private static String WORKPATH = "/";

    @Autowired
    ApiServerService _apiServer;

    public ApiServlet() {

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());

        ServletContext servletCtx = config.getServletContext();
        // 初始化路径
        // 保存文件的目录
        WORKPATH = servletCtx.getRealPath("/");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        StringBuffer auditTrailSb = new StringBuffer();
        auditTrailSb.append(StringPool.SPACE + req.getRemoteAddr());
        auditTrailSb.append(" -- " + req.getMethod() + " -- ");

        Map<String, Object[]> params = Maps.newHashMap();
        params.putAll(req.getParameterMap());

        Map<String, String> cookies = cookiesToMap(req.getCookies());
        String sessionKey = cookies.get(ApiConstants.SESSION_KEY);

        String reqStr = auditTrailSb.toString();
        LOG.debug("===START=== " + reqStr);

        try {
            Object[] commandObj = params.get("command");
            if (commandObj != null && StringUtils.isNotBlank((String) commandObj[0])) {
                String command = (String) commandObj[0];
                if (ApiConstants.LOGOUT_COMMAND.equalsIgnoreCase(command)) {

                    _apiServer.userLogout(sessionKey);

                    auditTrailSb.append("command=" + ApiConstants.LOGOUT_COMMAND);
                    auditTrailSb.append(StringPool.SPACE  + HttpServletResponse.SC_OK);

                    writeResponse(resp, getLogoutResponse(), HttpServletResponse.SC_OK);
                    return;
                } else if (ApiConstants.LOGIN_COMMAND.equalsIgnoreCase(command)) {
                    auditTrailSb.append("command=" + ApiConstants.LOGIN_COMMAND);

                    String[] username = (String[]) params.get("username");
                    String[] password = (String[]) params.get("password");

                    if (username != null) {
                        String pwd = ((password == null) ? null : password[0]);
                        try {
                            String loginSessionKey = _apiServer.userLogin(username[0], pwd);
                            writeResponse(resp, "{\" " + ApiConstants.SESSION_KEY + " \":\""+loginSessionKey+"\"}", 200);
                            return;
                        } catch (ApolloAuthenticationException e) {
                            auditTrailSb.append(e.getMessage() != null ? e.getMessage() : "无法验证用户，检查用户名/密码是正确的");
                            String serializedResponse = getSerializedApiError(ApiErrorCode.ACCOUNT_ERROR.getHttpCode(), e.getMessage() != null ? e.getMessage() : "无法验证用户，检查用户名/密码是正确的");
                            writeResponse(resp, serializedResponse, ApiErrorCode.ACCOUNT_ERROR.getHttpCode());
                            return;
                        }
                    }
                }
            } else {
                auditTrailSb.append(StringPool.SPACE  + HttpServletResponse.SC_BAD_REQUEST + " command为空");
                String serializedResponse = getSerializedApiError(HttpServletResponse.SC_BAD_REQUEST, "command为空");
                writeResponse(resp, serializedResponse, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (_apiServer.verifyUser(sessionKey)) {
                //验证sessionKey是否为null
                if (StringUtils.isNotBlank(sessionKey)) {
                    params.put(ApiConstants.SESSION_KEY, new String[] { sessionKey });
                }
                if (_apiServer.verifyRequest(params)) {
                    params.put(ApiConstants.HTTP_METHOD, new String[] { req.getMethod() });

                    String response;
                    if (req.getContentType() != null && req.getContentType().toLowerCase(Locale.ENGLISH).startsWith(MULTIPART)) {

                        //包含文件
                        Map<String, String> fileMap = UploadFile.uploadFile(req, WORKPATH);

                        response = _apiServer.handleFileRequest(params, auditTrailSb, fileMap);
                    }else {
                        response = _apiServer.handleRequest(params, auditTrailSb);
                    }
                    writeResponse(resp, response != null ? response : "", HttpServletResponse.SC_OK);
                } else {
                    auditTrailSb.append(StringPool.SPACE  + HttpServletResponse.SC_UNAUTHORIZED + " 无法验证用户凭证或没有命令执行权限");
                    String serializedResponse = getSerializedApiError(HttpServletResponse.SC_UNAUTHORIZED, "无法验证用户凭证或没有命令执行权限");
                    writeResponse(resp, serializedResponse, HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else {
                auditTrailSb.append(StringPool.SPACE  + HttpServletResponse.SC_UNAUTHORIZED + " sessionKey无效");
                String serializedResponse = getSerializedApiError(HttpServletResponse.SC_UNAUTHORIZED, "sessionKey无效");
                writeResponse(resp, serializedResponse, HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (ApiException e) {
            String serializedResponseText = getSerializedApiError(e);
            resp.setHeader("X-Description", e.getMessage());
            writeResponse(resp, serializedResponseText, e.getErrorCode().getHttpCode());
            auditTrailSb.append(StringPool.SPACE + e.getErrorCode() + StringPool.SPACE + e.getMessage());
        } catch (Exception e) {
            LOG.error("响应出现未知异常", e);
            auditTrailSb.append("响应出现未知异常");
        } finally {
            ACCESS_LOG.info(auditTrailSb.toString());
            LOG.debug("===END=== " + reqStr);
        }
    }

    private void writeResponse(HttpServletResponse resp, String response, int responseCode) {
        try {
            resp.setContentType("text/javascript; charset=UTF-8");
            resp.setStatus(responseCode);
            resp.getWriter().print(response);
        } catch (IOException e) {
            LOG.trace("输出API响应IO错误: " + e);
        } catch (Exception e) {
            if (!(e instanceof IllegalStateException)) {
                LOG.error("输出API响应时出现了未知错误", e);
            }
        }
    }

    private Map<String, String> cookiesToMap(Cookie[] cookies) {
        Map<String, String> map = Maps.newHashMap();
        if (cookies == null) {
            return map;
        }
        for (Cookie cookie : cookies) {
            map.put(cookie.getName(), cookie.getValue());
        }
        return map;
    }

    public String getSerializedApiError(int errorCode, String errorText) {
        return "{ \"errorCode\" : " + errorCode + ", \"errorText\" : \"" + errorText + "\" }";
    }

    public String getSerializedApiError(ApiException ex) {
        String responseText = null;

        if (ex == null) {
            return "{ \"errorCode\" : " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR + ", \"errorText\" : \"发生了内部错误\" }";
        }
        try {
            responseText = getSerializedApiError(ex.getErrorCode().getHttpCode(), ex.getMessage());

        } catch (Exception e) {
            LOG.error("响应HTTP请求出现错误", e);
        }
        return responseText;
    }

    private String getLogoutResponse() {
        return "{ \"success\": true }";
    }
}
