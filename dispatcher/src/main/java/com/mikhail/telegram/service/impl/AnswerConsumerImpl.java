package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.controller.UpdateProcessor;
import com.mikhail.telegram.service.AnswerConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@RequiredArgsConstructor
@Service
public class AnswerConsumerImpl implements AnswerConsumer {

    private final UpdateProcessor updateProcessor;

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.answer-message}")
    public void consume(SendMessage sendMessage) {
        // Считывает из брокера ответы, которые были отправлены из NODE
        // И пересылает их в API Telegram
        // Ответы находятся в отдельной очереди ANSWER_MESSAGE

        updateProcessor.setView(sendMessage);
    }
}
