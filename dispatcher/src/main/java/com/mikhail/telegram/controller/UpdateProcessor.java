package com.mikhail.telegram.controller;

import com.mikhail.telegram.service.UpdateProducer;
import com.mikhail.telegram.utils.MessageUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.mikhail.telegram.model.RabbitQueue.*;

@Component
@Log4j
public class UpdateProcessor {

    private TelegramBot bot;
    private final MessageUtils messageUtils;

    private final UpdateProducer updateProducer;

    public UpdateProcessor(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramBot bot) {
        this.bot = bot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.hasMessage()) {
            distributeMessagesByType(update);
        } else {
            log.error("Unsupported message type is received " + update);
        }
    }

    private void distributeMessagesByType(Update update) {
        Message message = update.getMessage();
        if (update.hasMessage()
                && message.hasText()
        ) {
            processTextMessage(update);
        } else if (message.hasPhoto()) {
            processPhotoMessage(update);
        } else if (message.hasDocument()) {
            processDocMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setFileIsReceivedView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Файл получен! Обрабатывается...");
        setView(sendMessage);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void setUnsupportedMessageTypeView(Update update) {
        SendMessage sendMessage = this.messageUtils.generateSendMessageWithText(
                update,
                "Неподдерживаемый тип сообщения!"
        );
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        this.bot.sendAnswerMessage(sendMessage);
    }
}
