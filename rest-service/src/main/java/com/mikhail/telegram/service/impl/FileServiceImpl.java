package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.dao.AppDocumentDAO;
import com.mikhail.telegram.dao.AppPhotoDAO;
import com.mikhail.telegram.entity.AppDocument;
import com.mikhail.telegram.entity.AppPhoto;
import com.mikhail.telegram.service.FileService;
import com.mikhail.telegram.utils.CryptoTool;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Log4j
@Service
public class FileServiceImpl implements FileService {

    private final AppPhotoDAO appPhotoDAO;

    private final AppDocumentDAO appDocumentDAO;

    private final CryptoTool cryptoToolPhoto;

    private final CryptoTool cryptoToolDoc;

    public FileServiceImpl(AppPhotoDAO appPhoto,
                           AppDocumentDAO appDocumentDAO,
                           @Qualifier("CryptoToolPhoto") CryptoTool cryptoToolPhoto,
                           @Qualifier("CryptoToolDoc") CryptoTool cryptoToolDoc
    ) {
        this.appPhotoDAO = appPhoto;
        this.appDocumentDAO = appDocumentDAO;
        this.cryptoToolPhoto = cryptoToolPhoto;
        this.cryptoToolDoc = cryptoToolDoc;
    }

    @Override
    public AppDocument getDocument(String docHash) {

        Long id = cryptoToolDoc.idOf(docHash);
        if (id == null) {
            return null;
        }

        return appDocumentDAO.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String photoHash) {
        Long id = cryptoToolPhoto.idOf(photoHash);
        if (id == null) {
            return null;
        }

        return appPhotoDAO.findById(id).orElse(null);
    }
}
