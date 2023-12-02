package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.service.ConsumerService;
import com.mikhail.telegram.service.MainService;
import com.mikhail.telegram.service.ProducerService;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.mikhail.telegram.model.RabbitQueue.*;

@Service
@Log4j
public class ConsumerServiceImpl implements ConsumerService {

    private final MainService mainService;

    private final ProducerService producerService;

    public ConsumerServiceImpl(MainService mainService, ProducerService producerService) {
        this.mainService = mainService;
        this.producerService = producerService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdates(Update update) {
        log.debug("NODE: Text message is received");

        // получает сообщения из соответствующей очереди
        // сохраняет сообщение в виде JSON в БД
        // формирует ответ на сообщение и публикует его в очередь ANSWER_MESSAGE
        // ответы накапливаются в этой очереди и будут обработаны в микросервисе dispatcher
        mainService.processTextMessage(update);
    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumeDocsMessageUpdates(Update update) {
        log.debug("NODE: Doc message is received");

        mainService.processDocMessage(update);
    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessageUpdates(Update update) {
        log.debug("NODE: Photo message is received");

        mainService.processPhotoMessage(update);
    }
}
