package com.mikhail.telegram.controller;

import com.mikhail.telegram.config.BotConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;

    private UpdateController updateController;

    public TelegramBot(BotConfig config,
                       UpdateController updateController
    ) {
        super(config.getBotToken());

        this.config = config;
        this.updateController = updateController;
    }

    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {

        System.out.println("test");


        Message receivedMessage = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            receivedMessage = update.getMessage();
            String messageText = receivedMessage.getText();

            log.debug("Text: " + messageText);

            SendMessage responseMessage = new SendMessage();
            responseMessage.setChatId(receivedMessage.getChatId());
            responseMessage.setText("Hello!");
            sendAnswerMessage(responseMessage);
        }






    }

    public void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}
