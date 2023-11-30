package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.controller.UpdateController;
import com.mikhail.telegram.service.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.mikhail.telegram.model.RabbitQueue.ANSWER_MESSAGE;

@Service
public class AnswerConsumerImpl implements AnswerConsumer {

    private final UpdateController updateController;

    public AnswerConsumerImpl(UpdateController updateController) {
        this.updateController = updateController;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        // Считывает из брокера ответы, которые были отправлены из NODE
        // И пересылает их в API Telegram
        // Ответы находятся в отдельной очереди ANSWER_MESSAGE

        updateController.setView(sendMessage);
    }
}
