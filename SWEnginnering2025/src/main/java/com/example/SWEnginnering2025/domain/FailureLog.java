/*Project: AttachmentController.java
        Author: 한지윤
        Date of creation: 2025.11.23
        Date of last update: 2025.11.23
                */

package com.example.SWEnginnering2025.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "failure_log")
@Getter
public class FailureLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long failureId;

    private Long userId;

    //목표별 아이디
    private Long goalId;

    // 실패가 발생한 날짜 (캘린더/분석용)
    private LocalDate failedDate;

    //실패에 대한 메모(예: 시험 준비 때문에 피곤해서 못함)
    @Column(columnDefinition = "TEXT")
    private String memo;

    //실패가 발생한 정확한 시간
    @Column(nullable = false)
    private LocalDateTime failedAt;

    @ManyToMany
    @JoinTable(
            name = "failure_log_tag_map",
            joinColumns = @JoinColumn(name = "failure_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<FailureTag> tags = new HashSet<>();

    protected FailureLog() {
    }

    @Builder
    public FailureLog(Long userId,
                      Long goalId,
                      LocalDate failedDate,
                      String memo,
                      LocalDateTime failedAt) {
        this.userId = userId;
        this.goalId = goalId;
        this.failedDate = failedDate;
        this.memo = memo;
        this.failedAt = failedAt;
    }

    public void addTag(FailureTag tag) {
        this.tags.add(tag);
    }
}
