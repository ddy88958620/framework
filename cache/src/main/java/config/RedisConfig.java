package config;

import com.google.common.base.Preconditions;
import com.handu.apollo.config.service.easyzk.ConfigNode;
import com.handu.apollo.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by markerking on 14-4-22.
 */
@Configuration
public class RedisConfig {

    @Autowired
    Environment env;
    @Autowired(required = false)
    @Qualifier("applicationConfigNode")
    private ConfigNode applicationConfigNode;

    private static final String DEFAULT_PORT = "6379";
    private static final String DEFAULT_USE_POOL = "true";

    @Bean
    public JedisConnectionFactory jredisConnectionFactory() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        final String hostname = Preconditions.checkNotNull(StringUtil.getProperty(applicationConfigNode, env, "redis.host"), "redis.host is null");
        final String port = StringUtil.getProperty(applicationConfigNode, env, "redis.port", DEFAULT_PORT);
        final String usePool = StringUtil.getProperty(applicationConfigNode, env, "redis.usePool", DEFAULT_USE_POOL);

        connectionFactory.setHostName(hostname);
        connectionFactory.setPort(Integer.parseInt(port));
        connectionFactory.setUsePool(Boolean.parseBoolean(usePool));
        return connectionFactory;
    }

    @Bean
    public StringRedisSerializer getRedisSerializer(){
        return new StringRedisSerializer();
    }

    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(jredisConnectionFactory());
        template.setValueSerializer(getRedisSerializer());
        template.setKeySerializer(getRedisSerializer());
        return template;
    }

}
