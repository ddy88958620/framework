package com.handu.apollo.mq.rocket;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by markerking on 15/1/4.
 */
@Configuration
public class DemoRocketProducerConfig {
    public static final String DEFAULT_PRODUCER_GROUP_NAME = "DefaultProducerGroupName";
    public static final String DEFAULT_NAME_SRV_ADDR = "172.16.1.75:9876";

    /**
     * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
     * 注意：ProducerGroupName需要由应用来保证唯一<br>
     * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
     * 因为服务器会回查这个Group下的任意一个Producer<br>
     * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
     * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从RocketMQ服务器上注销自己
     */
    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public MQProducer applicationRocketMQProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(DEFAULT_PRODUCER_GROUP_NAME);
        producer.setNamesrvAddr(DEFAULT_NAME_SRV_ADDR);
        return producer;
    }
}
