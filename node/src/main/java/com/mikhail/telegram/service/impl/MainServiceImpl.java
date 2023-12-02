package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.dao.AppUserDAO;
import com.mikhail.telegram.dao.RawDataDAO;
import com.mikhail.telegram.entity.AppUser;
import com.mikhail.telegram.entity.RawData;
import com.mikhail.telegram.entity.UserState;
import com.mikhail.telegram.service.MainService;
import com.mikhail.telegram.service.ProducerService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static com.mikhail.telegram.entity.UserState.BASIC_STATE;
import static com.mikhail.telegram.entity.UserState.WAIT_FOR_EMAIL_STATE;
import static com.mikhail.telegram.service.enums.ServiceCommands.*;

@Service
@Log4j
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

        AppUser appUser = findOrSaveAppUser(update);
        UserState userState = appUser.getState();
        String textCommand = update.getMessage().getText();

        String output = null;

        if (CANCEL.equals(textCommand)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, textCommand);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            //todo добавить проверку email, сохранение в БД или вывод сообщения об ошибке
        } else {
            log.error("Unknown user state: " + userState);
            output = "Неизвестная ошибка. Введите " + CANCEL + " и попробуйте повторить операцию.";
        }

        Long chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        AppUser appUser = findOrSaveAppUser(update);
        Long chatId = update.getMessage().getChatId();

        if (!isAllowToSendContent(chatId, appUser)) {
            return;
        }

        //todo реализовать сохранение фото

        String output = "Фотография загружена успешно. Ссылка для скачивания: test.ru/photo";
        sendAnswer(output, chatId);
    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        AppUser appUser = findOrSaveAppUser(update);
        Long chatId = update.getMessage().getChatId();

        if (!isAllowToSendContent(chatId, appUser)) {
            return;
        }

        //todo реализовать сохранение документа

        String output = "Документ загружен успешно. Ссылка для скачивания: test.ru/doc";
        sendAnswer(output, chatId);
    }

    private boolean isAllowToSendContent(Long chatId, AppUser appUser) {

        UserState userState = appUser.getState();
        if (!appUser.getIsActive()) {
            String error = "Зарегистрируйтесь или активируйте "
                    + "свою учетную запись для загрузки контента.";
            sendAnswer(error, chatId);
            return false;
        } else if (!BASIC_STATE.equals(userState)) {
            String error = "Для отправки файлов отмените текущую команду с помощью "
                    + CANCEL + " .";
            sendAnswer(error, chatId);
            return false;
        }
        return true;
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);

        producerService.produceAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String textCommand) {
        if (REGISTRATION.equals(textCommand)) {
            //todo реализовать
            return "Временно недоступно";
        } else if (HELP.equals(textCommand)) {
            return help();
        } else if (START.equals(textCommand)) {
            return "Добро пожаловать! Чтобы узнать список доступных команд воспользуйтесь " + HELP;
        } else {
            return "Неизвестная команда! Чтобы узнать список доступных команд воспользуйтесь " + HELP;
        }
    }

    private String help() {
        return "Список доступных команд:\n"
                + CANCEL + " - отмена выполнения текущей команды;\n"
                + REGISTRATION + " - регистрация пользователя.";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserDAO.save(appUser);

        return "Команда отменена.";
    }

    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();

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
