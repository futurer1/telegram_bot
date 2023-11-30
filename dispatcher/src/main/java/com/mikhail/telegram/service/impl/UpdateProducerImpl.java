package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.service.UpdateProducer;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Log4j
public class UpdateProducerImpl implements UpdateProducer {

    private final RabbitTemplate rabbitTemplate;

    public UpdateProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String rabbitQueue, Update update) {
        log.debug(update.getMessage().getText());

        // преобразование в JSON объекта Update и отправка в указанную очередь rabbitQueue

        // Отправляет полученное от API Telegram сообщение в брокера
        // Сообщения пользователей накапливаются в различных очередях в зависимости от
        // типа сообщения
        rabbitTemplate.convertAndSend(rabbitQueue, update);
    }
}
