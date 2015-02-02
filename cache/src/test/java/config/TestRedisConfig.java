package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by markerking on 14-4-22.
 */
@Configuration
@PropertySource("classpath:/application.properties")
@ComponentScan(basePackages = {"com.handu.apollo.cache"})
public class TestRedisConfig {

    @Autowired
    Environment env;

    @Bean
    public JedisConnectionFactory jredisConnectionFactory() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(env.getProperty("redis.host"));
        connectionFactory.setPort(Integer.parseInt(env.getProperty("redis.port")));
        connectionFactory.setUsePool(Boolean.parseBoolean(env.getProperty("redis.usePool")));
        return connectionFactory;
    }

    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(jredisConnectionFactory());
        return template;
    }

}
