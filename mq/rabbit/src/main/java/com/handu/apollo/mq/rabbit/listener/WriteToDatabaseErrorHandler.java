package com.handu.apollo.mq.rabbit.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handu.apollo.data.Dao;
import com.handu.apollo.data.utils.Param;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.StringPool;
import com.handu.apollo.utils.json.JsonUtil;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.util.ErrorHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by markerking on 14/12/22.
 */
public class WriteToDatabaseErrorHandler implements ErrorHandler {

    private static final Log LOG = Log.getLog(WriteToDatabaseErrorHandler.class);
    private static final String CLASSNAME = WriteToDatabaseErrorHandler.class.getName() + StringPool.PERIOD;

    private Dao dao;
    private DefaultMessageConverter messageConverter = new DefaultMessageConverter();
    private ObjectMapper objectMapper = JsonUtil.getMapper();

    public WriteToDatabaseErrorHandler(Dao dao) {
        this.dao = dao;
    }

    /**
     * 捕获异常并写入数据库，如果写库失败则再次抛出异常
     * 写库成功后不再重复处理此消息
     *
     * @param t
     */
    @Override
    public void handleError(Throwable t) {
        LOG.warn("Execution of Rabbit message listener failed.", t);
        if (!this.causeChainContainsARADRE(t)) {
            ListenerExecutionFailedException lefe = (ListenerExecutionFailedException) t;
            Message failedMessage = lefe.getFailedMessage();
            if (this.writeToDatabase(failedMessage)) {
                throw new AmqpRejectAndDontRequeueException("Write failed message to database", t);
            }
        } else {
            throw new AmqpRejectAndDontRequeueException("Error Handler converted exception to fatal", t);
        }
    }

    /**
     * 将消息体和消息属性JSON序列化后写入数据库
     *
     * @param message
     */
    private boolean writeToDatabase(Message message) {
        if (this.dao == null) {
            return false;
        }
        try {
            Map<String, Object> params = new Param.Builder()
                    .put("id", UUID.randomUUID().toString())
                    .put("body", objectMapper.writeValueAsString(messageConverter.fromMessage(message)))
                    .put("properties", objectMapper.writeValueAsString(message.getMessageProperties()))
                    .build();
            this.dao.insert(CLASSNAME, "add", params);
            return true;
        } catch (Exception e) {
            LOG.warn("Write failed message to database failed.", e);
            return false;
        }
    }

    /**
     * @return true 如果包含AmqpRejectAndDontRequeueException
     */
    private boolean causeChainContainsARADRE(Throwable t) {
        Throwable cause = t.getCause();
        while (cause != null) {
            if (cause instanceof AmqpRejectAndDontRequeueException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public DefaultMessageConverter getMessageConverter() {
        return messageConverter;
    }

    public void setMessageConverter(DefaultMessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    private class DefaultMessageConverter extends Jackson2JsonMessageConverter {

        private DefaultClassMapper defaultClassMapper;

        public DefaultMessageConverter() {
            super();
            initializeClassMapper();
        }

        protected void initializeClassMapper() {
            defaultClassMapper = new DefaultClassMapper();
            defaultClassMapper.setDefaultType(HashMap.class);
            defaultClassMapper.setDefaultHashtableClass(HashMap.class);
            this.setClassMapper(this.defaultClassMapper);
        }
    }
}
