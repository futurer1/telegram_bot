package com.mikhail.telegram.service;

import com.mikhail.telegram.entity.AppDocument;
import com.mikhail.telegram.entity.AppPhoto;

public interface FileService {
    AppDocument getDocument(String id);

    AppPhoto getPhoto(String id);
}
