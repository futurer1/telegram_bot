package com.mikhail.telegram.controller;

import com.mikhail.telegram.config.BotConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j
@Component
public class TelegramBot extends TelegramWebhookBot {
    private final BotConfig config;

    private final static String UPDATE_PATH = "/update";

    private final UpdateProcessor updateProcessor;

    public TelegramBot(BotConfig config,
                       UpdateProcessor updateProcessor
    ) {
        super(config.getBotToken());

        this.config = config;
        this.updateProcessor = updateProcessor;
    }

    @PostConstruct
    public void init() {
        updateProcessor.registerBot(this);

        try {
            SetWebhook setWebhook = SetWebhook.builder()
                    .url(config.getBotUri())
                    .build();
            this.setWebhook(setWebhook);

            log.debug("Dispatcher is start");
        } catch (TelegramApiException e) {
            log.error(e);
        }
    }

    public void sendAnswerMessage(SendMessage message) {
        if (!config.getBotMuteAnswer()) {
            if (message != null) {
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    @Override
    public String getBotPath() {
        return UPDATE_PATH;
    }
}
