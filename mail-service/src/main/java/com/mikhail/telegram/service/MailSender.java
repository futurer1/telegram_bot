package com.mikhail.telegram.service;

import com.mikhail.telegram.dto.MailParams;

public interface MailSender {
    void send(MailParams mailParams);
}
