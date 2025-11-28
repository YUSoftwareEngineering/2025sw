/*Project: AttachmentController.java
        Author: 한지윤
        Date of creation: 2025.11.28
        Date of last update: 2025.11.28
                */
package com.example.SWEnginnering2025.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "routine")
@Getter
@NoArgsConstructor
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // null이면 공통 루틴, 커스텀이면 userId

    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private FailureCategory targetCategory;

    private String recommendedTime;

    private int difficultyLevel;

    public Routine(Long userId, String title, String description,
                   FailureCategory targetCategory, String recommendedTime, int difficultyLevel) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.targetCategory = targetCategory;
        this.recommendedTime = recommendedTime;
        this.difficultyLevel = difficultyLevel;
    }
}
