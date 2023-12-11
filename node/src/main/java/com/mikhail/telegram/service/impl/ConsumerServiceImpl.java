package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.service.ConsumerService;
import com.mikhail.telegram.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
@Service
public class ConsumerServiceImpl implements ConsumerService {

    private final MainService mainService;

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.text-message-update}")
    public void consumeTextMessageUpdates(Update update) {
        log.debug("NODE: Text message is received");

        // получает сообщения из соответствующей очереди
        // сохраняет сообщение в виде JSON в БД
        // формирует ответ на сообщение и публикует его в очередь ANSWER_MESSAGE
        // ответы накапливаются в этой очереди и будут обработаны в микросервисе dispatcher
        mainService.processTextMessage(update);
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.doc-message-update}")
    public void consumeDocsMessageUpdates(Update update) {
        log.debug("NODE: Doc message is received");

        mainService.processDocMessage(update);
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.photo-message-update}")
    public void consumePhotoMessageUpdates(Update update) {
        log.debug("NODE: Photo message is received");

        mainService.processPhotoMessage(update);
    }
}
