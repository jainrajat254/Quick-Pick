package com.rajat.quickpick.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;


public class CollegeDtos {

    @Data
    public static class CollegeCreateDto {

        @NotBlank(message = "College name is required")
        private String name;

        @NotBlank(message = "Address is required")
        private String address;

        @NotBlank(message = "City is required")
        private String city;

        @NotBlank(message = "State is required")
        private String state;
    }

    @Data
    public static class CollegeResponseDto {

        private String id;
        private String name;
        private String address;
        private String city;
        private String state;
    }
}
