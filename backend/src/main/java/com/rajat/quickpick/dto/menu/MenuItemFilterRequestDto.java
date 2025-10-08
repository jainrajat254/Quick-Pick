package com.rajat.quickpick.dto.menu;

import com.rajat.quickpick.model.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemFilterRequestDto {
    private List<MenuItem> menuItems;
    private MenuItemSearchDto criteria;
}
