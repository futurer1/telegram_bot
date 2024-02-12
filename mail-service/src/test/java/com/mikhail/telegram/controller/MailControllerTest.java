package com.mikhail.telegram.controller;

import com.mikhail.telegram.dto.MailParams;
import com.mikhail.telegram.service.impl.MailSenderImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

@ExtendWith(MockitoExtension.class)
public class MailControllerTest {

    @Mock
    MailSenderImpl mailSender;

    @InjectMocks
    MailController controller;

    @Test
    public void sendActivationMailTest() {

        Random random = new Random();
        int id = random.nextInt(100);

        MailParams mailParams = new MailParams(
                String.valueOf(id),
                "test@mail.ru"
        );

        controller.sendActivationMail(mailParams);
    }
}
