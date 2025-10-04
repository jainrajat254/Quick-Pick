package com.rajat.quickpick.dto.admin;

import com.rajat.quickpick.enums.Role;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminUserDto {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String collegeName;
    private String department;
    private String studentId;
    private Role role;
    private boolean emailVerified;
    private boolean phoneVerified;
    private boolean suspended;
    private String suspensionReason;
    private LocalDateTime suspendedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}