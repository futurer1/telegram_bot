package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.dao.AppUserDAO;
import com.mikhail.telegram.dto.MailParams;
import com.mikhail.telegram.entity.AppUser;
import com.mikhail.telegram.service.AppUserService;
import com.mikhail.telegram.utils.CryptoTool;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.util.Optional;

import static com.mikhail.telegram.entity.UserState.BASIC_STATE;
import static com.mikhail.telegram.entity.UserState.WAIT_FOR_EMAIL_STATE;
import static com.mikhail.telegram.service.enums.ServiceCommands.CANCEL;

@Log4j
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserDAO appUserDAO;

    private final CryptoTool cryptoToolUserId;

    @Value("${service.mail.uri}")
    private String mailServiceUri;

    public AppUserServiceImpl(AppUserDAO appUserDAO,
                              @Qualifier("CryptoToolUserId") CryptoTool cryptoToolUserId
    ) {
        this.appUserDAO = appUserDAO;
        this.cryptoToolUserId = cryptoToolUserId;
    }

    @Override
    public String registryUser(AppUser appUser) {
        if (appUser.getIsActive()) {
            return "Вы уже зарегистрированы!";
        } else if (appUser.getEmail() != null) {
            return "Письмо со ссылкой для активации аккаунта уже было отправлено на email " + appUser.getEmail();
        }

        appUser.setState(WAIT_FOR_EMAIL_STATE);
        appUserDAO.save(appUser);

        return "Введите ваш email для регистрации:";
    }

    @Override
    public String setEmail(AppUser appUser, String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();

        } catch (AddressException e) {
            return "Введите корректный email.\nДля отмены введите "+ CANCEL;
        }

        Optional<AppUser> appUserOptional = appUserDAO.findUserByEmail(email);

        if (appUserOptional.isEmpty()) {
            appUser.setEmail(email);
            appUser.setState(BASIC_STATE);
            appUserDAO.save(appUser);

            String cryptoUserId = cryptoToolUserId.hashOf(appUser.getId());
            var response = sendRequestToMailService(cryptoUserId, email);
            if (response.getStatusCode() != HttpStatus.OK) {
                String out = String.format("Не удалось отправить письмо на ящик %s!", email);
                log.error(out);
                appUser.setEmail(null);
                appUserDAO.save(appUser);

                return out;
            }

            return "На ящик " + email + " было отправлено письмо.\nЗавершите регистрацию переходом по ссылке из письма.";
        } else {

            appUser.setState(BASIC_STATE);
            appUserDAO.save(appUser);

            return "Данный email уже был использован для регистрации.";
        }
    }

    private ResponseEntity<String> sendRequestToMailService(String cryptoUserId, String email) {


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MailParams mailParams = MailParams.builder()
                .id(cryptoUserId)
                .recipientEmail(email)
                .build();

        HttpEntity<MailParams> request = new HttpEntity<MailParams>(mailParams, headers);

        return restTemplate.exchange(
                mailServiceUri,
                HttpMethod.POST,
                request,
                String.class
        );
    }
}
