package com.mikhail.telegram.service;

import com.mikhail.telegram.entity.AppDocument;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message externalMessage);
}
