package com.rajat.quickpick.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemsResponseDto {
    private List<MenuItemResponseDto> menuItems;
    private int count;
}

