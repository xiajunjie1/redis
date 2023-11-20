package com.maker.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 *定义消息监听处理类
 *
 */

@Configuration
public class RedisSubscribeConfig {
    @Bean
    public RedisMessageListenerContainer listenerContainer(
            @Autowired
            @Qualifier("lettuceConnectionFactory")
            RedisConnectionFactory connectionFactory
    ){
        RedisMessageListenerContainer container =new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //如果想要配置通道，那么需要使用Topic
        List<Topic> topicList=new ArrayList<>(); //订阅通道集合
       // topicList.add(new PatternTopic("xia:*")); //通过正则匹配配置通道
        topicList.add(new ChannelTopic("channel:xia"));//全字匹配
        container.addMessageListener(this.messageListener(),topicList);
        return container;
    }

    @Bean
    public MessageListener messageListener(){
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                System.out.printf("【消息订阅】通道：%s，消息：%s\n"
                        ,new String(message.getChannel()),new String(message.getBody()));
            }
        };
    }
}
