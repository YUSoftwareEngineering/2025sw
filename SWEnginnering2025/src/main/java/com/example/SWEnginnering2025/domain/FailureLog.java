/*Project: AttachmentController.java
        Author: 한지윤
        Date of creation: 2025.11.23
        Date of last update: 2025.11.23
                */
package com.example.SWEnginnering2025.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "failure_log")
@NoArgsConstructor
@Setter
@Getter
public class FailureLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long goalId;

    // 실패가 발생한 날짜 (달력 기준)
    @Column(nullable = false)
    private LocalDate failedDate;

    // 실제 실패 기록 시간 (타임라인용)
    @Column(nullable = false)
    private LocalDateTime failedAt;

    // 자유 메모
    @Column(length = 500)
    private String memo;

    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "failure_log_tag_map",
            joinColumns = @JoinColumn(name = "failure_log_id"),
            inverseJoinColumns = @JoinColumn(name = "failure_tag_id")
    )
    private Set<FailureTag> tags = new HashSet<>();

    // ★ FailureLogService.logFailure 에서 쓰는 생성자
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

    // ★ 태그 추가 메서드 (FailureLogService에서 log::addTag 로 사용)
    public void addTag(FailureTag tag) {
        this.tags.add(tag);
    }
}
