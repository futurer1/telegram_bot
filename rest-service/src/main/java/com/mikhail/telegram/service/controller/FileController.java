package com.mikhail.telegram.service.controller;

import com.mikhail.telegram.entity.AppDocument;
import com.mikhail.telegram.entity.AppPhoto;
import com.mikhail.telegram.entity.BinaryContent;
import com.mikhail.telegram.service.FileService;
import lombok.extern.log4j.Log4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j
@RequestMapping("/file")
@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/download-doc")
    public ResponseEntity<?> getDocument(@RequestParam(name = "id") String docId) {

        AppDocument appDocument = fileService.getDocument(docId);
        if (appDocument == null) {
            return ResponseEntity.badRequest().build();
        }

        BinaryContent binaryContent = appDocument.getBinaryContent();
        var fileSystemResource = fileService.getFileSystemResource(binaryContent);

        if (fileSystemResource == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(appDocument.getMimeType())
                )
                .header("Content-disposition", "attachment; filename="
                        + appDocument.getDocName())
                .body(fileSystemResource);


    }

    @GetMapping("/download-photo")
    public ResponseEntity<?> getPhoto(@RequestParam(name = "id") String photoId) {

        AppPhoto appPhoto = fileService.getPhoto(photoId);
        if (appPhoto == null) {
            return ResponseEntity.badRequest().build();
        }

        BinaryContent binaryContent = appPhoto.getBinaryContent();
        var fileSystemResource = fileService.getFileSystemResource(binaryContent);

        if (fileSystemResource == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE)
                )
                .header("Content-disposition", "attachment;")
                .body(fileSystemResource);
    }
}
