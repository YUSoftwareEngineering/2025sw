/*Project: AttachmentController.java
        Author: 이채민/한지윤
        Date of creation: 2025.11.22
        Date of last update: 2025.11.23
                */

package com.example.SWEnginnering2025.controller;

import com.example.SWEnginnering2025.domain.AchievementColor;
import com.example.SWEnginnering2025.dto.CreateGoalRequest;
import com.example.SWEnginnering2025.dto.GoalBulkUpdateRequest;
import com.example.SWEnginnering2025.dto.GoalResponse;
import com.example.SWEnginnering2025.dto.GoalStatusRequest;
import com.example.SWEnginnering2025.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    /**
     * 1. 목표 생성
     *  - 임시로 userId = 1L 사용 (나중에 인증 붙이면 교체)
     *  - 생성된 Goal의 ID만 반환
     */
    @PostMapping
    public ResponseEntity<Long> createGoal(@RequestBody @Valid CreateGoalRequest request) {
        Long userId = 1L; // TODO: 인증 붙으면 실제 로그인 유저 ID 사용
        Long goalId = goalService.createGoal(request, userId);
        return ResponseEntity.ok(goalId);
    }

    /**
     * 2. 목표 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<GoalResponse> updateGoal(
            @PathVariable Long id,
            @RequestBody @Valid CreateGoalRequest request
    ) {
        GoalResponse response = goalService.updateGoal(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 3. 목표 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 4. 단일 목표 상태 변경 (실패 메모/인증샷 포함)
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody GoalStatusRequest request
    ) {
        goalService.updateStatus(id, request);
        return ResponseEntity.ok().build();
    }

    /**
     * 5. 목표 일괄 상태 변경 (Bulk)
     */
    @PatchMapping("/status/bulk")
    public ResponseEntity<Void> updateStatusBulk(
            @RequestBody GoalBulkUpdateRequest request
    ) {
        goalService.updateStatusBulk(request);
        return ResponseEntity.ok().build();
    }

    /**
     * 6. 날짜 범위별 목표 조회
     *  - 17번 기능: 이전/다음 주, 이전/다음 월 이동 시 이 API만 다시 호출하면 됨
     *  - 예: GET /api/v1/goals?startDate=2025-11-01&endDate=2025-11-30
     */
    @GetMapping
    public ResponseEntity<List<GoalResponse>> getGoalsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        Long userId = 1L; // TODO: 인증 연동 후 교체
        List<GoalResponse> goals = goalService.getGoalsByDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(goals);
    }

    /**
     * 7. 날짜별 성과 색상 조회 (파랑/노랑/빨강/회색)
     *  - 20번 기능: 캘린더 원 색깔 표시
     *  - 예: GET /api/v1/goals/achievement?date=2025-11-25
     */
    @GetMapping("/achievement")
    public ResponseEntity<AchievementColor> getAchievementColor(
            @RequestParam LocalDate date
    ) {
        AchievementColor color = goalService.getAchievementColor(date);
        return ResponseEntity.ok(color);
    }
}