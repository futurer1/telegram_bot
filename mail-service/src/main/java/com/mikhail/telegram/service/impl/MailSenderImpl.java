package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.dto.MailParams;
import com.mikhail.telegram.service.MailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderImpl implements MailSender {

    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderBotEmail;

    @Value("${service.activation.uri}")
    private String activationServiceUri;

    public MailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(MailParams mailParams) {
        String subject = "Активация учетной записи для бота";
        String messageBody = getActivationMessageBody(mailParams.getId());
        String recipientMail = mailParams.getRecipientEmail();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(senderBotEmail);
        mailMessage.setTo(recipientMail);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        mailSender.send(mailMessage);
    }

    private String getActivationMessageBody(String id) {
        return String.format("Для регистрации в телеграмм боте подтвердите свой email перейдя по ссылке:\n%s",
                activationServiceUri.replace("{id}", id)
        );
    }
}
