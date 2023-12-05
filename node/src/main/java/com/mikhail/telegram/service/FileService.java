package com.mikhail.telegram.service;

import com.mikhail.telegram.entity.AppDocument;
import com.mikhail.telegram.entity.AppPhoto;
import com.mikhail.telegram.service.enums.LinkType;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message externalMessage);

    AppPhoto processPhoto(Message telegramMessage);

    String generateLink(Long id, LinkType linkType);
}
