package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.dao.AppUserDAO;
import com.mikhail.telegram.dao.RawDataDAO;
import com.mikhail.telegram.entity.AppDocument;
import com.mikhail.telegram.entity.AppPhoto;
import com.mikhail.telegram.entity.AppUser;
import com.mikhail.telegram.entity.RawData;
import com.mikhail.telegram.entity.UserState;
import com.mikhail.telegram.exceptions.UploadFileException;
import com.mikhail.telegram.service.AppUserService;
import com.mikhail.telegram.service.MainService;
import com.mikhail.telegram.service.ProducerService;
import com.mikhail.telegram.service.enums.LinkType;
import com.mikhail.telegram.service.enums.ServiceCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import com.mikhail.telegram.service.FileService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

import static com.mikhail.telegram.entity.UserState.BASIC_STATE;
import static com.mikhail.telegram.entity.UserState.WAIT_FOR_EMAIL_STATE;
import static com.mikhail.telegram.service.enums.ServiceCommands.*;

@Log4j
@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService {

    private final AppUserDAO appUserDAO;

    private final RawDataDAO rawDataDAO;

    private final ProducerService producerService;

    private final FileService fileService;

    private final AppUserService appUserService;

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        AppUser appUser = findOrSaveAppUser(update);
        UserState userState = appUser.getState();
        String textCommand = update.getMessage().getText();

        String output = null;

        ServiceCommands serviceCommand = ServiceCommands.fromValue(textCommand);

        if (CANCEL.equals(serviceCommand)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, textCommand);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            output = appUserService.setEmail(appUser, textCommand);
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

        try {
            AppPhoto photo = fileService.processPhoto(update.getMessage());

            String link = fileService.generateLink(photo.getId(), LinkType.GET_PHOTO);

            String output = "Фотография загружена успешно. Ссылка для скачивания: " + link;
            sendAnswer(output, chatId);
        } catch (UploadFileException e) {
            log.error(e);
            String error = "В ходе загрузки фото произошла ошибка. Повторите попытку позже.";
            sendAnswer(error, chatId);
        }
    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        AppUser appUser = findOrSaveAppUser(update);
        Long chatId = update.getMessage().getChatId();

        if (!isAllowToSendContent(chatId, appUser)) {
            return;
        }

        try {
            AppDocument doc = fileService.processDoc(update.getMessage());

            String link = fileService.generateLink(doc.getId(), LinkType.GET_DOC);

            var output = "Документ загружен успешно. "
                    + "Ссылка для скачивания: " + link;
            sendAnswer(output, chatId);
        } catch (UploadFileException e) {
            log.error(e);
            String error = "В ходе загрузки файла произошла ошибка. Повторите попытку позже.";
            sendAnswer(error, chatId);
        }
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
        ServiceCommands serviceCommands = ServiceCommands.fromValue(textCommand);

        if (REGISTRATION.equals(serviceCommands)) {
            return appUserService.registryUser(appUser);
        } else if (HELP.equals(serviceCommands)) {
            return help();
        } else if (START.equals(serviceCommands)) {
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

        Optional<AppUser> appUserOptional = appUserDAO.findUserByTelegramUserId(telegramUser.getId());

        if (appUserOptional.isEmpty()) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    // пользователь активен только после подтверждения email
                    .isActive(false)
                    .state(BASIC_STATE)
                    .build();

            return appUserDAO.save(transientAppUser);
        }

        return appUserOptional.get();
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
