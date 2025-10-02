package com.rajat.quickpick.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "colleges")
public class College {

    @Id
    private String id;

    private String name;
    private String address;
    private String city;
    private String state;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}