package com.rajat.quickpick.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokensDto {

    private String accessToken;

    private String refreshToken;

    private long expiresIn; // in seconds

    private String tokenType;

}

