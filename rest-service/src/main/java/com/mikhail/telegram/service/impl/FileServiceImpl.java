package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.dao.AppDocumentDAO;
import com.mikhail.telegram.dao.AppPhotoDAO;
import com.mikhail.telegram.entity.AppDocument;
import com.mikhail.telegram.entity.AppPhoto;
import com.mikhail.telegram.entity.BinaryContent;
import com.mikhail.telegram.service.FileService;
import com.mikhail.telegram.utils.CryptoTool;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Log4j
@Service
public class FileServiceImpl implements FileService {

    private final AppPhotoDAO appPhotoDAO;

    private final AppDocumentDAO appDocumentDAO;

    private final CryptoTool cryptoTool;

    public FileServiceImpl(AppPhotoDAO appPhoto, AppDocumentDAO appDocumentDAO, CryptoTool cryptoTool) {
        this.appPhotoDAO = appPhoto;
        this.appDocumentDAO = appDocumentDAO;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public AppDocument getDocument(String docHash) {

        Long id = cryptoTool.idOf(docHash);
        if (id == null) {
            return null;
        }

        return appDocumentDAO.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String photoHash) {
        Long id = cryptoTool.idOf(photoHash);
        if (id == null) {
            return null;
        }

        return appPhotoDAO.findById(id).orElse(null);
    }

    @Override
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try {

            // временный файл
            File temp = File.createTempFile("tempFile", ".bin");

            // удаление при завершении приложения
            temp.deleteOnExit();

            FileUtils.writeByteArrayToFile(temp, binaryContent.getFileAsArrayOfBytes());
            return new FileSystemResource(temp);
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }
}