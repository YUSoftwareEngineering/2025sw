/*Project: AttachmentController.java
        Author: 이채민/한지윤
        Date of creation: 2025.11.22
        Date of last update: 2025.11.23
                */
package com.example.SWEnginnering2025.service;

import com.example.SWEnginnering2025.domain.AchievementColor;
import com.example.SWEnginnering2025.domain.Goal;
import com.example.SWEnginnering2025.domain.GoalStatus;
import com.example.SWEnginnering2025.dto.CreateGoalRequest;
import com.example.SWEnginnering2025.dto.GoalBulkUpdateRequest;
import com.example.SWEnginnering2025.dto.GoalResponse;
import com.example.SWEnginnering2025.dto.GoalStatusRequest;
import com.example.SWEnginnering2025.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;

    /**
     * 1. 목표 생성 (유저 ID를 받아서 생성)
     */
    @Transactional
    public Long createGoal(CreateGoalRequest request, Long userId) {

        // (선택) 중복 체크 – 같은 날 같은 제목 목표가 이미 있으면 에러
        boolean isDuplicate = goalRepository.existsByUserIdAndTargetDateAndTitle(
                userId, request.getTargetDate(), request.getTitle());

        if (isDuplicate) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 유사한 목표가 있습니다.");
        }

        // 기본 상태 PENDING으로 생성
        Goal goal = new Goal(
                userId,
                request.getTitle(),
                request.getCategory(),
                GoalStatus.PENDING,
                request.getTargetDate()
        );

        // 나머지 필드(설명, 알림 여부, 시간) 세팅
        goal.update(
                request.getTitle(),
                request.getDescription(),
                request.getCategory(),
                request.getTargetDate(),
                request.isNotificationEnabled(),
                request.getScheduledTime()
        );

        goalRepository.save(goal);
        return goal.getId();
    }

    /**
     * 2. 목표 수정
     */
    @Transactional
    public GoalResponse updateGoal(Long id, CreateGoalRequest request) {
        Goal goal = findGoalById(id);

        goal.update(
                request.getTitle(),
                request.getDescription(),
                request.getCategory(),
                request.getTargetDate(),
                request.isNotificationEnabled(),
                request.getScheduledTime()
        );

        return GoalResponse.from(goal);
    }

    /**
     * 3. 목표 삭제
     */
    @Transactional
    public void deleteGoal(Long id) {
        Goal goal = findGoalById(id);
        goalRepository.delete(goal);
    }

    /**
     * 4. 단일 목표 상태 변경 (실패 메모/인증샷 포함)
     */
    @Transactional
    public void updateStatus(Long goalId, GoalStatusRequest request) {
        Goal goal = findGoalById(goalId);
        goal.changeStatus(request.getStatus(), request.getStatusMemo(), request.getProofUrl());
    }

    /**
     * 5. 목표 일괄 상태 변경 (Bulk Update)
     *    - 메모, 인증샷 없이 상태만 변경
     */
    @Transactional
    public void updateStatusBulk(GoalBulkUpdateRequest request) {
        for (Long id : request.getIds()) {
            Goal goal = findGoalById(id);
            goal.changeStatus(request.getStatus(), null, null);
        }
    }

    /**
     * 6. 실패 기록용 – Goal을 FAILED로 표시
     *    (FailureLogService에서 호출하는 용도)
     */
    @Transactional
    public void markGoalAsFailed(Long goalId) {
        Goal goal = findGoalById(goalId);
        goal.changeStatus(GoalStatus.FAILED, "FailureLogService 자동 기록", null);
    }

    /**
     * 내부 공통 – ID로 Goal 찾기
     */
    private Goal findGoalById(Long id) {
        return goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 목표가 없습니다. ID=" + id));
    }

    /**
     * 7. 특정 날짜의 성과 색상 조회 (파랑/노랑/빨강/회색)
     */
    @Transactional(readOnly = true)
    public AchievementColor getAchievementColor(LocalDate date) {
        Long userId = 1L; // 임시 유저 ID (나중에 인증 붙이면 교체)

        List<Goal> goals = goalRepository.findAllByUserIdAndTargetDate(userId, date);

        if (goals.isEmpty()) {
            return AchievementColor.GREY;
        }

        long completedCount = goals.stream()
                .filter(goal -> goal.getStatus() == GoalStatus.COMPLETED)
                .count();

        if (completedCount == goals.size()) {
            return AchievementColor.BLUE;
        } else if (completedCount == 0) {
            return AchievementColor.RED;
        } else {
            return AchievementColor.YELLOW;
        }
    }

    /**
     * 8. 날짜 범위로 목표 목록 조회 (17번: 주/월 이동용 핵심 메서드)
     */
    @Transactional(readOnly = true)
    public List<GoalResponse> getGoalsByDateRange(Long userId, LocalDate start, LocalDate end) {
        return goalRepository.findByUserIdAndTargetDateBetween(userId, start, end)
                .stream()
                .map(GoalResponse::from)
                .toList();
    }
}
