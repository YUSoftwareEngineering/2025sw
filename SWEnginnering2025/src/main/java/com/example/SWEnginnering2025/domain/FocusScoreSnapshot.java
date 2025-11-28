/*Project: AttachmentController.java
        Author: 한지윤
        Date of creation: 2025.11.28
        Date of last update: 2025.11.28
                */
package com.example.SWEnginnering2025.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "focus_score_snapshot")
@Getter
@NoArgsConstructor
public class FocusScoreSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDate date;

    private int focusScore; // 0~100

    public FocusScoreSnapshot(Long userId, LocalDate date, int focusScore) {
        this.userId = userId;
        this.date = date;
        this.focusScore = focusScore;
    }
}
