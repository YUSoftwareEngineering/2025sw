package com.example.SWEnginnering2025.dto;

import com.example.SWEnginnering2025.domain.DailyGoal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class DailyGoalResponse {
    private Long id;
    private String title;
    private String description;
    private boolean isCompleted;
    private LocalDateTime createdAt;

    // Entity -> DTO 변환기 (공장)
    public static DailyGoalResponse from(DailyGoal entity) {
        return DailyGoalResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .isCompleted(entity.getIsCompleted())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}