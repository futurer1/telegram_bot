package com.mikhail.telegram.service.controller;

import com.mikhail.telegram.entity.AppDocument;
import com.mikhail.telegram.entity.AppPhoto;
import com.mikhail.telegram.entity.BinaryContent;
import com.mikhail.telegram.service.FileService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Log4j
@RequestMapping("/file")
@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/download-doc")
    public void getDocument(@RequestParam(name = "id") String docId,
                                         HttpServletResponse response
    ) {

        AppDocument appDocument = fileService.getDocument(docId);
        if (appDocument == null) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        response.setContentType(
                MediaType.parseMediaType(appDocument.getMimeType()).toString());
        response.setHeader(
                "Content-disposition", "attachment; filename="
                        + appDocument.getDocName()
        );
        response.setStatus(HttpServletResponse.SC_OK);

        BinaryContent binaryContent = appDocument.getBinaryContent();

        try (ServletOutputStream out = response.getOutputStream()) {
            out.write(binaryContent.getFileAsArrayOfBytes());
        } catch (IOException e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download-photo")
    public void getPhoto(@RequestParam(name = "id") String photoId,
                                      HttpServletResponse response
    ) {

        AppPhoto appPhoto = fileService.getPhoto(photoId);
        if (appPhoto == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        response.setHeader("Content-disposition", "attachment;");
        response.setStatus(HttpServletResponse.SC_OK);

        BinaryContent binaryContent = appPhoto.getBinaryContent();

        try (ServletOutputStream out = response.getOutputStream()) {
            out.write(binaryContent.getFileAsArrayOfBytes());
        } catch (IOException e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
