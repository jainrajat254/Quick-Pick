package com.rajat.quickpick.dto.user;

import com.rajat.quickpick.enums.Role;
import lombok.Data;

@Data
public class UserResponseDto {

    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String collegeName;
    private String profileImageUrl;
    private Role role;
    private boolean isPhoneVerified;
    private boolean isEmailVerified;
}