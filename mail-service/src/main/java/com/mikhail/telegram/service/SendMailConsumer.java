package com.mikhail.telegram.service;

import com.mikhail.telegram.dto.MailParams;

public interface SendMailConsumer {
    void consumeNewMail(MailParams mailParams);
}
