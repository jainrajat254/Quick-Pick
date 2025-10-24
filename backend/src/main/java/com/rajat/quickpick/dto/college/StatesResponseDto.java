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
public class StatesResponseDto {
    private List<String> states;
    private int count;
}


