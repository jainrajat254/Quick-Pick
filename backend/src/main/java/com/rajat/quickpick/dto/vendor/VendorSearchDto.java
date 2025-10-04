package com.rajat.quickpick.dto.vendor;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class VendorSearchDto {
    private String searchQuery;
}

