# 阿波罗集中权限管理

## 配置

1. maven引入apollo-security
2. application.properties增加配置，具体地址视情况而定

```
mdm.url=http://192.168.1.195:8080/mdm

shiro.redis.host=192.168.1.205
shiro.redis.port=6379
shiro.redisManager.expire=1800
```

3. web.xml中增加如下配置

```xml
<filter>
    <filter-name>shiroFilter</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    <init-param>
        <param-name>targetFilterLifecycle</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>

<filter-mapping>
    <filter-name>shiroFilter</filter-name>
    <url-pattern>/mvc/*</url-pattern>
</filter-mapping>
```

4. 实现MvcServerService或ApiServerService，并根据实际情况编写权限验证代码，可参考[活动管理](http://192.168.1.202/erp/pam)
5. 引用配置类`ShiroClientConfig.class`
6. 增加拦截器

```java
@Bean
public RequestMappingHandlerMapping requestMappingHandlerMapping() {
    RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
    Object[] objects = new Object[] {
            loginStatusInterceptor()
    };
    mapping.setInterceptors(objects);
    return mapping;
}

@Bean
public LoginStatusInterceptor loginStatusInterceptor() {
    return new LoginStatusInterceptor();
}
```