## RokectMQ使用帮助

### 引入

```xml
<dependency>
    <groupId>com.handu.apollo</groupId>
    <artifactId>apollo-mq-rokect</artifactId>
</dependency>

### 发布端配置

```java
@Configuration
public class DemoRocketProducerConfig {
    public static final String DEFAULT_PRODUCER_GROUP_NAME = "DefaultProducerGroupName";
    public static final String DEFAULT_NAME_SRV_ADDR = "rocketmq-namesrv-01:9876,rocketmq-namesrv-02:9876,";

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
```

### 消费端配置

```java
@Configuration
public class DemoRocketConsumerConfig {
    public static final String DEFAULT_CONSUMER_GROUP_NAME = "DefaultConsumerGroupName";
    public static final String DEFAULT_NAME_SRV_ADDR = "rocketmq-namesrv-01:9876,rocketmq-namesrv-02:9876,";

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
```

### 订阅语法

可以用`||`分隔多个tag，例如：

```java
pushConsumer.subscribe("SomeTopic", "Tag1 || Tag2 || Tag3");
```

也可以订阅所有tag：

```java
pushConsumer.subscribe("SomeTopic", "*");
```

也可以订阅多个topic：

```java
pushConsumer.subscribe("SomeTopic1", "*");
pushConsumer.subscribe("SomeTopic2", "Tag1");
pushConsumer.subscribe("SomeTopic3", "Tag2 || Tag3");
```