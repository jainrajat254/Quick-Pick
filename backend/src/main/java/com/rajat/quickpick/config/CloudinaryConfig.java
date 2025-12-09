package com.rajat.quickpick.config;

import com.cloudinary.Cloudinary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryConfig.class);

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        logger.info("CLOUDINARY_IMAGE_DEBUG: Initializing Cloudinary bean");
        logger.info("CLOUDINARY_IMAGE_DEBUG: Cloud name configured: {}",
                   cloudName != null && !cloudName.isEmpty() ? cloudName : "NOT_SET");
        logger.info("CLOUDINARY_IMAGE_DEBUG: API key configured: {}",
                   apiKey != null && !apiKey.isEmpty() ? "SET (hidden)" : "NOT_SET");
        logger.info("CLOUDINARY_IMAGE_DEBUG: API secret configured: {}",
                   apiSecret != null && !apiSecret.isEmpty() ? "SET (hidden)" : "NOT_SET");

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);

        logger.info("CLOUDINARY_IMAGE_DEBUG: Creating Cloudinary instance with configuration");
        Cloudinary cloudinary = new Cloudinary(config);
        logger.info("CLOUDINARY_IMAGE_DEBUG: Cloudinary bean created successfully");

        return cloudinary;
    }
}
