package com.rajat.quickpick.dto.college;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCollegeDto {

    @NotBlank(message = "College name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

}