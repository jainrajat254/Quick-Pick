package com.rajat.quickpick.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.rajat.quickpick.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);

    private static final long MAX_FILE_SIZE = 1 * 1024 * 1024;
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"
    );

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
        logger.info("CLOUDINARY_IMAGE_DEBUG: CloudinaryService initialized");
    }

    public String uploadFile(MultipartFile file) throws IOException {
        logger.info("CLOUDINARY_IMAGE_DEBUG: uploadFile() method called");

        logger.info("CLOUDINARY_IMAGE_DEBUG: Step 1 - Checking if file is null or empty");
        if (file == null || file.isEmpty()) {
            logger.warn("CLOUDINARY_IMAGE_DEBUG: Validation failed - File is null or empty");
            throw new BadRequestException("File is required");
        }
        logger.info("CLOUDINARY_IMAGE_DEBUG: Step 1 passed - File is present");

        logger.info("CLOUDINARY_IMAGE_DEBUG: Step 2 - Checking file size");
        logger.info("CLOUDINARY_IMAGE_DEBUG: File size: {} bytes, Max allowed: {} bytes",
                   file.getSize(), MAX_FILE_SIZE);
        if (file.getSize() > MAX_FILE_SIZE) {
            logger.warn("CLOUDINARY_IMAGE_DEBUG: Validation failed - File size {} exceeds limit {}",
                       file.getSize(), MAX_FILE_SIZE);
            throw new BadRequestException("File size must be less than 5MB");
        }
        logger.info("CLOUDINARY_IMAGE_DEBUG: Step 2 passed - File size is acceptable");

        logger.info("CLOUDINARY_IMAGE_DEBUG: Step 3 - Checking content type");
        String contentType = file.getContentType();
        logger.info("CLOUDINARY_IMAGE_DEBUG: File content type: {}", contentType);
        logger.info("CLOUDINARY_IMAGE_DEBUG: Allowed content types: {}", ALLOWED_CONTENT_TYPES);
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            logger.warn("CLOUDINARY_IMAGE_DEBUG: Validation failed - Invalid content type: {}", contentType);
            throw new BadRequestException("Invalid file type. Only images (JPEG, PNG, GIF, WebP) are allowed");
        }
        logger.info("CLOUDINARY_IMAGE_DEBUG: Step 3 passed - Content type is valid");

        // Validation 4: Filename check
        logger.info("CLOUDINARY_IMAGE_DEBUG: Step 4 - Checking filename");
        String originalFilename = file.getOriginalFilename();
        logger.info("CLOUDINARY_IMAGE_DEBUG: Original filename: {}", originalFilename);
        if (originalFilename == null || originalFilename.isEmpty()) {
            logger.warn("CLOUDINARY_IMAGE_DEBUG: Validation failed - Filename is missing");
            throw new BadRequestException("Filename is required");
        }
        logger.info("CLOUDINARY_IMAGE_DEBUG: Step 4 passed - Filename is present");

        try {
            logger.info("CLOUDINARY_IMAGE_DEBUG: Step 5 - All validations passed, preparing Cloudinary upload");
            logger.info("CLOUDINARY_IMAGE_DEBUG: File details - Name: {}, Size: {} bytes, Type: {}",
                       originalFilename, file.getSize(), contentType);

            logger.info("CLOUDINARY_IMAGE_DEBUG: Step 6 - Creating upload options");
            Map<String, Object> uploadOptions = ObjectUtils.asMap(
                "folder", "quickpick",
                "resource_type", "image"
            );
            logger.info("CLOUDINARY_IMAGE_DEBUG: Upload options configured: folder=quickpick");

            logger.info("CLOUDINARY_IMAGE_DEBUG: Step 7 - Converting file to bytes");
            byte[] fileBytes = file.getBytes();
            logger.info("CLOUDINARY_IMAGE_DEBUG: File converted to {} bytes", fileBytes.length);

            logger.info("CLOUDINARY_IMAGE_DEBUG: Step 8 - Calling Cloudinary uploader");
            Map uploadResult = cloudinary.uploader().upload(fileBytes, uploadOptions);
            logger.info("CLOUDINARY_IMAGE_DEBUG: Cloudinary upload completed");
            logger.info("CLOUDINARY_IMAGE_DEBUG: Upload result keys: {}", uploadResult.keySet());

            logger.info("CLOUDINARY_IMAGE_DEBUG: Step 9 - Extracting secure URL from result");
            String secureUrl = (String) uploadResult.get("secure_url");
            logger.info("CLOUDINARY_IMAGE_DEBUG: Secure URL extracted: {}", secureUrl);

            logger.info("CLOUDINARY_IMAGE_DEBUG: Step 10 - Upload process completed successfully");
            logger.info("CLOUDINARY_IMAGE_DEBUG: Final URL to return: {}", secureUrl);

            return secureUrl;

        } catch (IOException e) {
            logger.error("CLOUDINARY_IMAGE_DEBUG: IOException occurred during Cloudinary upload");
            logger.error("CLOUDINARY_IMAGE_DEBUG: Exception message: {}", e.getMessage());
            logger.error("CLOUDINARY_IMAGE_DEBUG: Exception type: {}", e.getClass().getName());
            logger.error("CLOUDINARY_IMAGE_DEBUG: Stack trace:", e);
            throw new IOException("Failed to upload image to cloud storage: " + e.getMessage(), e);
        } catch (Exception e) {

            throw new IOException("Failed to upload image to cloud storage: " + e.getMessage(), e);
        }
    }
}
