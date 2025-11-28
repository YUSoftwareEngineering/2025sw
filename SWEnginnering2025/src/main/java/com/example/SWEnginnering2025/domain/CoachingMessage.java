/*Project: AttachmentController.java
        Author: 한지윤
        Date of creation: 2025.11.28
        Date of last update: 2025.11.28
                */
package com.example.SWEnginnering2025.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "coaching_message")
@Getter
@NoArgsConstructor
public class CoachingMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private CoachingTone tone;

    // 🔹 가장 취약한 요일
    @Enumerated(EnumType.STRING)
    private Weekday weakestWeekday;

    // 🔹 가장 취약한 시간대
    @Enumerated(EnumType.STRING)
    private TimeSlot weakestTimeSlot;

    // 🔹 가장 취약한 실패 카테고리
    @Enumerated(EnumType.STRING)
    private FailureCategory weakestCategory;

    @Column(length = 2000)
    private String adviceText;

    @Column(columnDefinition = "TEXT")
    private String recommendedRoutinesJson;

    private LocalDateTime createdAt;

    private boolean delivered;

    private boolean read;

    public CoachingMessage(Long userId,
                           CoachingTone tone,
                           Weekday weakestWeekday,
                           TimeSlot weakestTimeSlot,
                           FailureCategory weakestCategory,
                           String adviceText,
                           String recommendedRoutinesJson,
                           LocalDateTime createdAt) {

        this.userId = userId;
        this.tone = tone;
        this.weakestWeekday = weakestWeekday;      // ✅ 필드와 동일한 이름
        this.weakestTimeSlot = weakestTimeSlot;    // ✅ 필드와 동일한 이름
        this.weakestCategory = weakestCategory;
        this.adviceText = adviceText;
        this.recommendedRoutinesJson = recommendedRoutinesJson;
        this.createdAt = createdAt;
        this.delivered = false;
        this.read = false;
    }
}

