## RabbitMQ使用帮助

### 引入

```xml
<dependency>
    <groupId>com.handu.apollo</groupId>
    <artifactId>apollo-mq-rabbit</artifactId>
</dependency>
```

### 增加配置项

```
rabbit.addresses=localhost
rabbit.username=admin
rabbit.password=password
```

### 消费端编写配置类

```java
@Configuration
@PropertySource("classpath:/app.properties")
public class Config extends AbstractRabbitConfig {
    @Override
    protected void configureRabbitTemplate(RabbitTemplate rabbitTemplate) {

    }

    @Bean
    public TopicExchange mdmDataExchange() {
        return new TopicExchange("exchange.mdm.data");
    }

    @Bean
    public Queue mdmDataQueue() {
        return new Queue("pam.queue.mdm.data");
    }

    @Bean
    public Binding mdmDataBinding() {
        return BindingBuilder.bind(mdmDataQueue()).to(mdmDataExchange()).with("Mis.*");
    }

    @Bean
    public SimpleMessageListenerContainer listenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames("queue.mdm.data");
        container.setMessageListener(new DemoMessageListener());
        return container;
    }
}
```

若想将失败消息记录到数据库，导入`mq_failed_message.sql`，并将SimpleMessageListenerContainer替换为如下代码:

```java
@Autowired
private Dao dao;

//不再重复上述配置...

@Bean
public ErrorHandler writeToDatabaseErrorHandler() {
    return new WriteToDatabaseErrorHandler(dao);
}

@Bean
public ApolloMessageListenerContainer listenerContainer() {
    ApolloMessageListenerContainer container = new ApolloMessageListenerContainer();
    container.setConnectionFactory(connectionFactory());
    container.setQueueNames("queue.mdm.data");
    container.setMessageListener(new DemoMessageListener());
    container.setErrorHandler(writeToDatabaseErrorHandler());
    return container;
}
```

DemoMessageListener:

```java
public class DemoMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println(message);
    }
}
```

### 配置说明

以上配置声明了一个Exchange和Queue的绑定，启动应用后，即可监听接收来自`exchange.mdm.data`的数据，应用在宕机时再恢复时，也会接收遗漏的信息。队列没有批量传递消息的功能，因此若想提高消费端的性能，可以将接收到的消息存放到临时列表中，等待一定时间后统一处理。

`TopicExchange`: 声明了一个主题交换，并且对应名称为`exchange.mdm.data`

`Queue`: 声明了一个队列，名称为`pam.queue.mdm.data`

`Binding`: 声明了一个绑定，将Exhange与Queue进行绑定关联，且订阅了`Mis.`开头的所有消息

`SimpleMessageListenerContainer`: 声明了一个或多个监听，可以将队列收到的数据交由此监听来处理

**假设**：主数据通过`exchange.mdm.data`发送所有数据的变更，消费者可以设定多个队列来接收消息，定义多个Queue和Binding，只是with的值不同，这样就可以将不同的消息设置给不同的监听来处理。

如果想临时接收消息，也就是说在消费端程序停止后，不再向队列中发送消息，可以将Queue定义为自动删除的，定义方法为`new Queue("pam.queue.mdm.data", true, false, true)`

### 路由匹配说明

# * (星号) 代表任意一个单词
# # (井号) 0个或者多个单词

案例：

routing_key包含三个单词和两个点号。第一个key是描述了celerity（敏捷），第二个是colour（色彩），第三个是species（物种）："<celerity>.<colour>.<species>"

在这里我们创建了两个绑定： Q1 的binding key 是`*.orange.*`； Q2 是  `*.*.rabbit` 和 `lazy.#`

比如routing_key是`quick.orange.rabbit`将会发送到Q1和Q2中。消息`lazy.orange.elephant`也会发送到Q1和Q2。但是`quick.orange.fox`会发送到Q1；`lazy.brown.fox`会发送到Q2。`lazy.pink.rabbit`也会发送到Q2，但是尽管两个routing_key都匹配，它也只是发送一次。`quick.brown.fox`会被丢弃。

如果发送的单词不是3个呢？答案要看情况，因为#是可以匹配0个或任意个单词。比如`orange`或者`quick.orange.male.rabbit`，它们会被丢弃。如果是lazy那么就会进入Q2。类似的还有`lazy.orange.male.rabbit`，尽管它包含四个单词。