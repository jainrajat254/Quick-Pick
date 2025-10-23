package com.rajat.quickpick.dto.college;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollegesResponseDto {
    private List<CollegeResponseDto> colleges;
    private int count;
}

