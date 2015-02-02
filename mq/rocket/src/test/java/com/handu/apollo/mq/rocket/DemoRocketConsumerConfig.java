package com.handu.apollo.mq.rocket;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.MQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListener;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.handu.apollo.mq.rocket.listener.ApolloMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by markerking on 15/1/4.
 */
@Configuration
public class DemoRocketConsumerConfig {
    public static final String DEFAULT_CONSUMER_GROUP_NAME = "DefaultConsumerGroupName";
    public static final String DEFAULT_NAME_SRV_ADDR = "172.16.1.75:9876";

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public MQPushConsumer demoPushConsumer() throws MQClientException {
        DefaultMQPushConsumer pushConsumer = new DefaultMQPushConsumer(DEFAULT_CONSUMER_GROUP_NAME);

        pushConsumer.setNamesrvAddr(DEFAULT_NAME_SRV_ADDR);

        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
         */
        pushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        // SomeTopic由producer发送消息时，Message对象定义
        pushConsumer.subscribe("SomeTopic", "*");

        pushConsumer.registerMessageListener(messageListener());

        return pushConsumer;
    }

    @Bean
    public MessageListener messageListener() {
        return new ApolloMessageListener() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                // 执行消费动作
                System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        };
    }
}
