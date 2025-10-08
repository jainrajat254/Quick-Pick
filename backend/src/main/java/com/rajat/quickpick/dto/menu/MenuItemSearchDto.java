package com.rajat.quickpick.dto.menu;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class MenuItemSearchDto {
    private String searchQuery;
    private String vendorId;
    private String category;
    private Double minPrice;
    private Double maxPrice;
    //implement Active offer later.....

    @Builder.Default
    private boolean availableOnly = true;

    @Builder.Default
    private String sortBy = "name";

    @Builder.Default
    private String sortDirection = "ASC";

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 20;

}
