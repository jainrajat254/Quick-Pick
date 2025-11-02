package com.rajat.quickpick.dto.auth;

import com.rajat.quickpick.enums.Role;
import lombok.Data;

@Data
public class AuthResponseDto {

    private TokensDto tokens;

    private String userId;
    private String email;
    private String name;
    private Role role;
    private String message;

}