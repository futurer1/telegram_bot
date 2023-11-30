package com.mikhail.telegram.controller;

import com.mikhail.telegram.service.UpdateProducer;
import com.mikhail.telegram.utils.MessageUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.mikhail.telegram.model.RabbitQueue.PHOTO_MESSAGE_UPDATE;
import static com.mikhail.telegram.model.RabbitQueue.TEXT_MESSAGE_UPDATE;

@Component
@Log4j
public class UpdateController {

    private TelegramBot bot;
    private final MessageUtils messageUtils;

    private final UpdateProducer updateProducer;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
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

        if (update.getMessage() != null) {
            distributeMessagesByType(update);
        } else {
            log.error("Received unsupported message type " + update);
        }
    }

    private void distributeMessagesByType(Update update) {
        Message message = update.getMessage();
        if (update.hasMessage()
                && update.getMessage().hasText()
        ) {
            processTextMessage(update);
        } else if (message.getPhoto() != null) {
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
    }

    private void setUnsupportedMessageTypeView(Update update) {
        SendMessage sendMessage = this.messageUtils.generateSendMessageWithText(
                update,
                "Неподдерживаемый тип сообщения!"
        );
        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        this.bot.sendAnswerMessage(sendMessage);
    }
}
