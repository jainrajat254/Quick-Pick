package com.rajat.quickpick.dto.admin;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminCollegeDto {
    private String id;
    private String name;
    private String address;
    private String city;
    private String state;
    private long totalUsers;
    private long totalVendors;
    private LocalDateTime createdAt;
}
