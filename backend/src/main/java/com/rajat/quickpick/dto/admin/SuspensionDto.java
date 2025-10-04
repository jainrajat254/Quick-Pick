package com.rajat.quickpick.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class SuspensionDto {
    @NotBlank(message = "Suspension reason is required")
    private String reason;
}