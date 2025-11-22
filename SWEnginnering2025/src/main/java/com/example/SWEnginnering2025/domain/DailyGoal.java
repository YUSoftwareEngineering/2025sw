package com.example.SWEnginnering2025.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity // 이 클래스가 DB 테이블이 됨을 명시
@Getter // 모든 필드의 Getter 메서드 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 (JPA 필수)
public class DailyGoal {

    @Id // Primary Key (기본키)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment (자동 번호 증가)
    private Long id; //  goalId에 해당

    // 문서의 userId (User 테이블과 연결되는 외래키 역할)
    // 실제로는 @ManyToOne으로 User 객체와 매핑해야 하지만, 지금은 ID만 저장합니다.
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String title; //  목표 이름

    @Column(columnDefinition = "TEXT")
    private String description; //  세부 설명

    @Column(nullable = false)
    private LocalDate targetDate; //  startDate/endDate를 하루 단위로 단순화

    //  status (ONGOING, COMPLETED 등)를 단순화하여 완료 여부로 구현
    @Column(nullable = false)
    private Boolean isCompleted = false;

    private LocalDateTime createdAt; //  생성 시각
    private LocalDateTime updatedAt; // 수정 시각

    @Builder // 빌더 패턴으로 객체 생성을 쉽게 함
    public DailyGoal(Long userId, String title, String description, LocalDate targetDate) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
        this.isCompleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 비즈니스 로직: 목표 수정 기능 (Setter 대신 사용)
    public void update(String title, String description, LocalDate targetDate) {
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
        this.updatedAt = LocalDateTime.now();
    }

    // 비즈니스 로직: 완료 상태 토글
    public void toggleStatus() {
        this.isCompleted = !this.isCompleted;
    }
}
