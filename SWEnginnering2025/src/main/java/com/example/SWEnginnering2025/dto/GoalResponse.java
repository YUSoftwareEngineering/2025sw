/*Project: AttachmentController.java
        Author: 이채민
        Date of creation: 2025.11.23
        Date of last update: 2025.11.23
                */

package com.example.SWEnginnering2025.dto;

import com.example.SWEnginnering2025.domain.Goal;
import com.example.SWEnginnering2025.domain.GoalCategory;
import com.example.SWEnginnering2025.domain.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalResponse {

    private Long id;
    private Long userId;

    private String title;
    private String description;

    private GoalStatus status;
    private String statusMemo;
    private String proofUrl;

    private GoalCategory category;

    private boolean isNotificationEnabled;
    private LocalTime scheduledTime;

    private LocalDateTime createdAt;
    private LocalDate targetDate;
    private LocalDateTime completedAt;

    public static GoalResponse from(Goal entity) {
        return GoalResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .status(entity.getStatus())
                .statusMemo(entity.getStatusMemo())
                .proofUrl(entity.getProofUrl())
                .targetDate(entity.getTargetDate())
                .createdAt(entity.getCreatedAt())
                .completedAt(entity.getCompletedAt())
                .isNotificationEnabled(entity.isNotificationEnabled())
                .scheduledTime(entity.getScheduledTime())
                .build();
    }
}
