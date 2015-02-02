# 阿波罗缓存

## 配置

1. maven引入apollo-cache
2. application.properties增加配置`redisDao.prefix=pam:`，根据各个项目的需求不同设置
3. 引用配置类`RedisConfig.class`
4. 注入RedisDao使用即可