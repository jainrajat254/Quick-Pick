package com.rajat.quickpick.dto.menu;


import lombok.Data;
import java.util.List;

@Data
public class VendorCategoryUpdateDto {
    private List<String> categories;
}