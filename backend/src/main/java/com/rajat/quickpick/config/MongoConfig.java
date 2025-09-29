package com.rajat.quickpick.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.rajat.quickpick.utils.Secrets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(Secrets.MONGODB_URI);
    }
}
