package com.example.nicbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {

    private Long totalRecords;
    private Long maleCount;
    private Long femaleCount;
}
