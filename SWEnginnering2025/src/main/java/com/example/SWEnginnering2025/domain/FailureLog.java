/*Project: AttachmentController.java
        Author: 한지윤
        Date of creation: 2025.11.23
        Date of last update: 2025.11.23
                */
package com.example.SWEnginnering2025.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "failure_log")
@Getter
@NoArgsConstructor
public class FailureLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    private LocalDateTime occurredAt;

    @Enumerated(EnumType.STRING)
    private Weekday weekday;

    @Enumerated(EnumType.STRING)
    private TimeSlot timeSlot;

    @ManyToMany
    @JoinTable(
            name = "failure_log_tag",
            joinColumns = @JoinColumn(name = "failure_log_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<FailureTag> tags;

    private String memo;

    public FailureLog(Long userId, Goal goal, LocalDateTime occurredAt,
                      Weekday weekday, TimeSlot timeSlot, List<FailureTag> tags, String memo) {
        this.userId = userId;
        this.goal = goal;
        this.occurredAt = occurredAt;
        this.weekday = weekday;
        this.timeSlot = timeSlot;
        this.tags = tags;
        this.memo = memo;
    }
}
