package com.mikhail.telegram.config;

import lombok.Getter;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class RabbitConfiguration {

    @Value("${spring.rabbitmq.queues.text-message-update}")
    private String queueTextMessageUpdate;

    @Value("${spring.rabbitmq.queues.doc-message-update}")
    private String queueDocMessageUpdate;

    @Value("${spring.rabbitmq.queues.photo-message-update}")
    private String queuePhotoMessageUpdate;

    @Value("${spring.rabbitmq.queues.answer-message}")
    private String queueAnswerMessage;

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue textMessageQueue() {
        return new Queue(queueTextMessageUpdate);
    }

    @Bean
    public Queue docMessageQueue() {
        return new Queue(queueDocMessageUpdate);
    }

    @Bean
    public Queue photoMessageQueue() {
        return new Queue(queuePhotoMessageUpdate);
    }

    @Bean
    public Queue answerMessageQueue() {
        return new Queue(queueAnswerMessage);
    }
}
