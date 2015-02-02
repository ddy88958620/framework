package com.handu.apollo.security.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.handu.apollo.config.service.easyzk.ConfigFactory;
import com.handu.apollo.config.service.easyzk.ConfigNode;
import com.handu.apollo.core.ApiConstants;
import com.handu.apollo.security.backend.SecurityBackend;
import com.handu.apollo.security.cache.RedisCacheManager;
import com.handu.apollo.security.cache.RedisManager;
import com.handu.apollo.security.cache.RedisSessionDAO;
import com.handu.apollo.security.eis.PasswordEncoder;
import com.handu.apollo.security.eis.SessionKeyGenerator;
import com.handu.apollo.security.realm.ApolloRealm;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.StringUtil;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

/**
 * Created by markerking on 14/10/22.
 */
@Configuration
@Import({LifecycleConfig.class})
public abstract class AbstractShiroConfig {
    private static final Log LOG = Log.getLog(AbstractShiroConfig.class);

    @Autowired
    Environment env;
    @Autowired(required = false)
    private ConfigFactory configFactory;

    public abstract SecurityBackend securityBackend();

    private static final String DEFAULT_PORT = "6379";
    private static final String DEFAULT_REDIS_MANAGER_EXPIRE = "1800";

    private static final String CONFIG_NODE_SHIRO = "shiro";

    private ConfigNode getShiroConfigNode() {
        try {
            return (configFactory != null ? configFactory.getConfigNode(CONFIG_NODE_SHIRO) : null);
        } catch (Exception e) {
            LOG.error("没有找到对应的ConfigNode: [{}]", CONFIG_NODE_SHIRO, e);
            Throwables.propagate(e);
        }
        return null;
    }

    @Bean
    public RedisManager redisManager() {
        ConfigNode configNode = this.getShiroConfigNode();
        final String host = Preconditions.checkNotNull(StringUtil.getProperty(configNode, env, "shiro.redis.host"), "shiro.redis.host is null");
        final int port = Integer.parseInt(StringUtil.getProperty(configNode, env, "shiro.redis.port", DEFAULT_PORT));
        final int expire = Integer.parseInt(StringUtil.getProperty(configNode, env, "shiro.redisManager.expire", DEFAULT_REDIS_MANAGER_EXPIRE));

        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setExpire(expire);

        return redisManager;
    }

    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(redisManager());

        return cacheManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager());
        sessionDAO.setSessionIdGenerator(new SessionKeyGenerator());

        return sessionDAO;
    }

    @Bean
    public SessionManager sessionManager() {
        ConfigNode configNode = this.getShiroConfigNode();
        final int expire = Integer.parseInt(StringUtil.getProperty(configNode, env, "shiro.redisManager.expire", DEFAULT_REDIS_MANAGER_EXPIRE));
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        SimpleCookie cookie = new SimpleCookie(ApiConstants.SESSION_KEY);
        cookie.setHttpOnly(false);
        //Cookie统一写到根下
        cookie.setPath("/");

        sessionManager.setSessionIdCookie(cookie);
        //与Redis统一超时时间
        sessionManager.setGlobalSessionTimeout(expire * 1000L);
        //检查Session超时的间隔
        sessionManager.setSessionValidationInterval(expire * 1000L);

        return sessionManager;
    }

    @Bean
    public HashedCredentialsMatcher sha512Matcher() {
        HashedCredentialsMatcher sha512Matcher = new HashedCredentialsMatcher();

        sha512Matcher.setHashAlgorithmName("SHA-512");
        sha512Matcher.setStoredCredentialsHexEncoded(false);
        sha512Matcher.setHashIterations(PasswordEncoder.DEFAULT_HASH_ITERATIONS);

        return sha512Matcher;
    }

    @Bean
    public AuthorizingRealm authorizingRealm() {
        AuthorizingRealm authorizingRealm = new ApolloRealm();
        authorizingRealm.setCredentialsMatcher(sha512Matcher());
        return authorizingRealm;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(authorizingRealm());
        securityManager.setCacheManager(cacheManager());
        securityManager.setSessionManager(sessionManager());

        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();

        shiroFilter.setSecurityManager(securityManager());

        return shiroFilter;
    }
}
