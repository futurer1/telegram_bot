package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.dao.AppPhotoDAO;
import com.mikhail.telegram.entity.AppPhoto;
import com.mikhail.telegram.service.enums.LinkType;
import com.mikhail.telegram.utils.CryptoTool;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.mikhail.telegram.dao.AppDocumentDAO;
import com.mikhail.telegram.dao.BinaryContentDAO;
import com.mikhail.telegram.entity.AppDocument;
import com.mikhail.telegram.entity.BinaryContent;
import com.mikhail.telegram.exceptions.UploadFileException;
import com.mikhail.telegram.service.FileService;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Log4j
@Service
public class FileServiceImpl implements FileService {
    @Value("${bot.token}")
    private String token;

    @Value("${service.file_info.uri}")
    private String fileInfoUri;

    @Value("${service.file_storage.uri}")
    private String fileStorageUri;

    @Value("${link.address}")
    private String linkAddress;
    private final AppDocumentDAO appDocumentDAO;

    private final AppPhotoDAO appPhotoDAO;
    private final BinaryContentDAO binaryContentDAO;
    private final CryptoTool cryptoToolDoc;

    private final CryptoTool cryptoToolPhoto;

    public FileServiceImpl(AppDocumentDAO appDocumentDAO, AppPhotoDAO appPhotoDAO, BinaryContentDAO binaryContentDAO, @Qualifier("CryptoToolDoc") CryptoTool cryptoToolDoc, @Qualifier("CryptoToolPhoto") CryptoTool cryptoToolPhoto) {
        this.appDocumentDAO = appDocumentDAO;
        this.appPhotoDAO = appPhotoDAO;
        this.binaryContentDAO = binaryContentDAO;
        this.cryptoToolDoc = cryptoToolDoc;
        this.cryptoToolPhoto = cryptoToolPhoto;
    }

    @Override
    public AppDocument processDoc(Message telegramMessage) {
        Document telegramDoc = telegramMessage.getDocument();
        String fileId = telegramDoc.getFileId();

        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {

            BinaryContent persistentBinaryContent = getPersistentBinaryContent(response);

            AppDocument transientAppDoc = buildTransientAppDoc(telegramDoc, persistentBinaryContent);
            return appDocumentDAO.save(transientAppDoc);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    @Override
    public AppPhoto processPhoto(Message telegramMessage) {

        // кол-во фото в соообщении
        int photoSizeCount = telegramMessage.getPhoto().size();

        //todo если к сообщению прикрепили несколько фото, то берем только одно последнее
        // последнее фото не сжатое и имеет самое большое разрешение
        int photoIndex = photoSizeCount > 1 ? telegramMessage.getPhoto().size() - 1 : 0;
        var telegramPhoto = telegramMessage.getPhoto().get(photoIndex);
        String fileId = telegramPhoto.getFileId();
        var response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {

            var persistentBinaryContent = getPersistentBinaryContent(response);

            AppPhoto transientAppPhoto = buildTransientAppPhoto(telegramPhoto, persistentBinaryContent);
            return appPhotoDAO.save(transientAppPhoto);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    /**
     * Получение объекта BinaryContent, который сохранен в БД, имеет id и связан с сессией JPA
     *
     * @param response
     * @return
     */
    private BinaryContent getPersistentBinaryContent(ResponseEntity<String> response) {
        String filePath = getFilePath(response);
        byte[] fileInByte = downloadFile(filePath);
        BinaryContent transientBinaryContent = BinaryContent.builder()
                .fileAsArrayOfBytes(fileInByte)
                .build();
        return binaryContentDAO.save(transientBinaryContent);
    }

    private AppPhoto buildTransientAppPhoto(PhotoSize telegramPhoto,
                                            BinaryContent persistentBinaryContent
    ) {
        return AppPhoto.builder()
                .telegramFileId(telegramPhoto.getFileId())
                .binaryContent(persistentBinaryContent)
                .fileSize(telegramPhoto.getFileSize())
                .build();
    }

    /**
     * Достает параметры из объекта Document телеграмма и сетит их в объект AppDocument
     *
     * @param telegramDoc объект API telegram
     * @param persistentBinaryContent бинарный контент документа уже связанный с нашей БД
     * @return
     */
    private AppDocument buildTransientAppDoc(Document telegramDoc,
                                             BinaryContent persistentBinaryContent
    ) {
        return AppDocument.builder()
                .telegramFileId(telegramDoc.getFileId())
                .docName(telegramDoc.getFileName())
                .binaryContent(persistentBinaryContent)
                .mimeType(telegramDoc.getMimeType())
                .fileSize(telegramDoc.getFileSize())
                .build();
    }

    /**
     * Делает HTTP запрос к телеграмму
     *
     * @param fileId id файла
     * @return
     */
    private ResponseEntity<String> getFilePath(String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                fileInfoUri,
                HttpMethod.GET,
                request,
                String.class,
                token,
                fileId
        );
    }


    /**
     * Получение пути к файлу для скачивания из telegram
     *
     * @param response
     * @return
     */
    private String getFilePath(ResponseEntity<String> response) {
        JSONObject jsonObject = new JSONObject(response.getBody());
        return String.valueOf(jsonObject
                .getJSONObject("result")
                .getString("file_path"));
    }

    /**
     * Формируется окончательный URI для скачивания файла с сервера телеграмм
     * добавлением пути к файлу и токена
     * @param filePath
     * @return
     */
    private byte[] downloadFile(String filePath) {
        String fullUri = fileStorageUri.replace("{token}", token)
                .replace("{filePath}", filePath);
        URL urlObj = null;
        try {
            urlObj = new URL(fullUri);
        } catch (MalformedURLException e) {
            throw new UploadFileException(e);
        }

        //TODO подумать над оптимизацией для скачивания больших файлов
        // файл вычитывается побайтово целиком в стриме
        try (InputStream is = urlObj.openStream()) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new UploadFileException(urlObj.toExternalForm(), e);
        }
    }

    @Override
    public String generateLink(Long id, LinkType linkType) {
        String hash = null;
        if (linkType == LinkType.GET_DOC) {
            hash = cryptoToolDoc.hashOf(id);
        } else if (linkType == LinkType.GET_PHOTO) {
            hash = cryptoToolPhoto.hashOf(id);
        }

        return "http://" + linkAddress + "/" + linkType + "?id=" + hash;
    }
}