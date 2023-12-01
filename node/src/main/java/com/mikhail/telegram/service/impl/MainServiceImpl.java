package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.dao.AppUserDAO;
import com.mikhail.telegram.dao.RawDataDAO;
import com.mikhail.telegram.entity.AppUser;
import com.mikhail.telegram.entity.RawData;
import com.mikhail.telegram.service.MainService;
import com.mikhail.telegram.service.ProducerService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static com.mikhail.telegram.entity.UserState.BASIC_STATE;

@Service
public class MainServiceImpl implements MainService {

    private final AppUserDAO appUserDAO;

    private final RawDataDAO rawDataDAO;

    private final ProducerService producerService;

    public MainServiceImpl(
            AppUserDAO appUserDAO,
            RawDataDAO rawDataDAO,
            ProducerService producerService
    ) {
        this.appUserDAO = appUserDAO;
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        Message message = update.getMessage();
        User telegramUser = message.getFrom();
        AppUser appUser = findOrSaveAppUser(telegramUser);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("Hello from NODE");

        producerService.produceAnswer(sendMessage);
    }

    private AppUser findOrSaveAppUser(User telegramUser) {
        AppUser persistentAppUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());
        if (persistentAppUser == null) {

            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //todo добавить поддержку активации через email
                    .isActive(true)
                    .state(BASIC_STATE)
                    .build();

            return appUserDAO.save(transientAppUser);
        }

        return persistentAppUser;
    }

    private void saveRawData(Update update) {
        // создание сущности из объекта Update
        // и ее сохранение в БД
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataDAO.save(rawData);
    }
}
