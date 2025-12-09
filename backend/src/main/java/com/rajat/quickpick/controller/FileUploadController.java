package com.rajat.quickpick.controller;

import com.rajat.quickpick.exception.BadRequestException;
import com.rajat.quickpick.service.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final CloudinaryService cloudinaryService;

    public FileUploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        logger.info("CLOUDINARY_IMAGE_DEBUG: Upload endpoint hit - POST /api/upload");
        logger.info("CLOUDINARY_IMAGE_DEBUG: Request received from authenticated user");

        try {
            logger.info("CLOUDINARY_IMAGE_DEBUG: Starting file upload process");
            logger.info("CLOUDINARY_IMAGE_DEBUG: File received - Name: {}, Size: {} bytes, ContentType: {}",
                    file != null ? file.getOriginalFilename() : "null",
                    file != null ? file.getSize() : 0,
                    file != null ? file.getContentType() : "null");

            String imageUrl = cloudinaryService.uploadFile(file);

            logger.info("CLOUDINARY_IMAGE_DEBUG: Image upload completed successfully");
            logger.info("CLOUDINARY_IMAGE_DEBUG: Returned URL: {}", imageUrl);
            logger.info("CLOUDINARY_IMAGE_DEBUG: Sending 200 OK response");

            return ResponseEntity.ok(imageUrl);

        } catch (BadRequestException e) {
            logger.warn("CLOUDINARY_IMAGE_DEBUG: Bad request exception caught - {}", e.getMessage());
            logger.warn("CLOUDINARY_IMAGE_DEBUG: Sending 400 BAD_REQUEST response");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (IOException e) {
            logger.error("CLOUDINARY_IMAGE_DEBUG: IO exception caught - {}", e.getMessage());
            logger.error("CLOUDINARY_IMAGE_DEBUG: Sending 500 INTERNAL_SERVER_ERROR response");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());

        } catch (Exception e) {
            logger.error("CLOUDINARY_IMAGE_DEBUG: Unexpected exception caught", e);
            logger.error("CLOUDINARY_IMAGE_DEBUG: Exception type: {}, Message: {}",
                    e.getClass().getName(), e.getMessage());
            logger.error("CLOUDINARY_IMAGE_DEBUG: Sending 500 INTERNAL_SERVER_ERROR response");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred during upload");
        }
    }
}
