package com.rajat.quickpick.dto.user;

import lombok.Data;

@Data
public class UpdateUserDto {

    private String fullName;
    private String phone;
    private String profileImageUrl;

}