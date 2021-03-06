package com.happy.server.config;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.happy.server.constant.MailConstants;
import com.happy.server.pojo.MailLog;
import com.happy.server.service.IMailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/03/06/17:35
 * @Description: RabbitMQ 配置类
 */
@Configuration
public class RabbitMQConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Bean
    public Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(){
        // 队列和交换机绑定
        return BindingBuilder.bind(queue())
                .to(exchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private IMailLogService mailLogService;

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        /**
         * 消息确认回调，确认消息是否到达 broker
         * correlationData：消息的唯一表示，发送消息的时候设置
         * ack：确认结果
         * cause：失败原因
         */
        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            if(ack){
                LOGGER.info("{}============>消息发送成功",correlationData.getId());
                // 如果消息确认成功，修改数据库消息的状态
                mailLogService.update(new UpdateWrapper<MailLog>()
                .set("status",1).eq("msgId",correlationData.getId()));
            }else {
                LOGGER.error("{}============>消息发送失败",correlationData.getId());
            }
        }));

        /**
         * 消息失败回调，比如 broker 找不到 queue 就会回调
         * msg：消息主题
         * repCode：响应码
         * repText：响应描述
         * exchange：交换机
         * routingKey：路由键
         */
        rabbitTemplate.setReturnCallback((msg,repCode,repText,exchange,routingKey)->{
            LOGGER.error("{}============>消息发送queue时失败",msg.getBody());
        });
        return rabbitTemplate;
    }

}
