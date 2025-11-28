/*Project: AttachmentController.java
        Author: 이채민
        Date of creation: 2025.11.22
        Date of last update: 2025.11.23
                */

package com.example.SWEnginnering2025.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "goal")
@Getter
@NoArgsConstructor
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private GoalCategory category;

    @Enumerated(EnumType.STRING)
    private GoalStatus status;

    @Column(length = 500)
    private String statusMemo;

    private String proofUrl;

    private LocalDate targetDate;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    private boolean notificationEnabled;
    private LocalTime scheduledTime;

    public Goal(Long userId, String title, GoalCategory category, GoalStatus status, LocalDate targetDate) {
        this.userId = userId;
        this.title = title;
        this.category = category;
        this.status = status;
        this.targetDate = targetDate;

        this.createdAt = LocalDateTime.now();
        this.notificationEnabled = false;
    }

    public void changeStatus(GoalStatus newStatus, String memo, String proofUrl) {
        this.status = newStatus;
        this.statusMemo = memo;
        this.proofUrl = proofUrl;

        if (newStatus == GoalStatus.COMPLETED) {
            this.completedAt = LocalDateTime.now();
        }
    }

    public void update(String title,
                       String description,
                       GoalCategory category,
                       LocalDate targetDate,
                       boolean notificationEnabled,
                       LocalTime scheduledTime) {

        this.title = title;
        this.description = description;
        this.category = category;
        this.targetDate = targetDate;
        this.notificationEnabled = notificationEnabled;
        this.scheduledTime = scheduledTime;
    }
}



