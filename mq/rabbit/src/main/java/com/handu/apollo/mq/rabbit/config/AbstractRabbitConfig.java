package com.handu.apollo.mq.rabbit.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.handu.apollo.config.service.easyzk.ConfigFactory;
import com.handu.apollo.config.service.easyzk.ConfigNode;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.StringUtil;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;

/**
 * Created by markerking on 14/10/8.
 */
@Configuration
public abstract class AbstractRabbitConfig {
    private static final Log LOG = Log.getLog(AbstractRabbitConfig.class);

    @Autowired
    Environment env;
    @Autowired(required = false)
    private ConfigFactory configFactory;

    protected abstract void configureRabbitTemplate(RabbitTemplate template);

    private static final String CONFIG_NODE_RABBITMQ = "rabbitmq";
    private static final String DEFAULT_CHANNEL_CACHE_SIZE = "25";

    @Bean
    public ConnectionFactory connectionFactory() {
        ConfigNode configNode = null;
        try {
            configNode = configFactory != null ? configFactory.getConfigNode(CONFIG_NODE_RABBITMQ) : null;
        } catch (Exception e) {
            LOG.error("没有找到对应的ConfigNode: [{}]", CONFIG_NODE_RABBITMQ, e);
            Throwables.propagate(e);
        }
        final String addresses = Preconditions.checkNotNull(StringUtil.getProperty(configNode, env, "rabbit.addresses"), "rabbit.addresses is null");
        final String username = Preconditions.checkNotNull(StringUtil.getProperty(configNode, env, "rabbit.username"), "rabbit.username is null");
        final String password = Preconditions.checkNotNull(StringUtil.getProperty(configNode, env, "rabbit.password"), "rabbit.password is null");
        final String channelCacheSize = StringUtil.getProperty(configNode, env, "rabbit.channelCacheSize", DEFAULT_CHANNEL_CACHE_SIZE);

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(addresses);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setChannelCacheSize(Integer.parseInt(channelCacheSize));

        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(jackson2JsonMessageConverter());
        configureRabbitTemplate(template);
        return template;
    }

    @Bean
    public ClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        // 设置默认类型
        classMapper.setDefaultType(HashMap.class);

        // 设置Class映射来解决类映射问题
//        Map<String, Class<?>> classMapping = Maps.newHashMap();
//        classMapping.put(Map.class.getCanonicalName(), Map.class);
//        classMapping.put(Hashtable.class.getCanonicalName(), HashMap.class);
//        classMapping.put(TreeMap.class.getCanonicalName(), TreeMap.class);
//        classMapping.put(HashMap.class.getCanonicalName(), HashMap.class);
//        classMapper.setIdClassMapping(classMapping);

        // 这样也可以避免Map转换为Hashtable而导致的无法解析null值
        classMapper.setDefaultHashtableClass(HashMap.class);

        return classMapper;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(classMapper());
        return converter;
    }

}