package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.dto.MailParams;
import com.mikhail.telegram.service.SendMailConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SendMailConsumerImpl implements SendMailConsumer {

    private final MailSenderImpl mailSender;

    @RabbitListener(queues = "${spring.rabbitmq.queues.mail-register}")
    @Override
    public void consumeNewMail(MailParams mailParams) {

        mailSender.send(mailParams);
    }
}
