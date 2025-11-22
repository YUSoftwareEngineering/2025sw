package com.example.SWEnginnering2025.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DailyGoalRequest {
    private String title;
    private String description;
    private LocalDate targetDate;
}