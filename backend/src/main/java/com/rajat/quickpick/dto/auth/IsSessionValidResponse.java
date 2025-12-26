package com.rajat.quickpick.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsSessionValidResponse {
    private boolean success;
    private int statusCode;
}
