package com.rajat.quickpick.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "razorpay")
public class RazorpayConfig {

    private String keyId;
    private String keySecret;
    private String environment;
    private String sandboxApiUrl;
    private String productionApiUrl;
    private String webhookSecret;

    public String getApiUrl() {
        if ("PRODUCTION".equalsIgnoreCase(environment)) {
            return productionApiUrl != null ? productionApiUrl : "https://api.razorpay.com/v1";
        }
        return sandboxApiUrl != null ? sandboxApiUrl : "https://api.razorpay.com/v1";
    }

    @Bean(name = "razorpayWebClient")
    public WebClient razorpayWebClient() {
        return WebClient.builder()
                .baseUrl(getApiUrl())
                .build();
    }
}

