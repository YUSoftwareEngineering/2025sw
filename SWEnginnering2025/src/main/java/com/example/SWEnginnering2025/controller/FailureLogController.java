/*Project: AttachmentController.java
        Author: 한지윤
        Date of creation: 2025.11.23
        Date of last update: 2025.11.23
                */

package com.example.SWEnginnering2025.controller;

import com.example.SWEnginnering2025.dto.failure.CreateTagRequest;
import com.example.SWEnginnering2025.dto.failure.FailureLogResponse;
import com.example.SWEnginnering2025.dto.failure.FailureTagDto;
import com.example.SWEnginnering2025.dto.failure.LogFailureRequest;
import com.example.SWEnginnering2025.service.FailureLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/failures")
@RequiredArgsConstructor
public class FailureLogController {

    private final FailureLogService failureLogService;

    // TODO: 나중에 인증 붙이면 실제 로그인 유저 ID 사용
    private static final Long TEMP_USER_ID = 1L;

    /**
     * 1) 실패 사유 태그 목록 조회
     *   - 기본 태그 + 사용자 커스텀 태그
     */
    @GetMapping("/tags")
    public ResponseEntity<List<FailureTagDto>> getFailureTags() {
        List<FailureTagDto> tags = failureLogService.getFailureTags(TEMP_USER_ID);
        return ResponseEntity.ok(tags);
    }

    /**
     * 2) 커스텀 태그 생성
     */
    @PostMapping("/tags")
    public ResponseEntity<FailureTagDto> createFailureTag(@RequestBody CreateTagRequest request) {
        FailureTagDto dto = failureLogService.createFailureTag(TEMP_USER_ID, request);
        return ResponseEntity.ok(dto);
    }

    /**
     * 3) 실패 기록 저장 + 해당 목표 상태 FAILED로 변경
     */
    @PostMapping
    public ResponseEntity<FailureLogResponse> logFailure(@RequestBody LogFailureRequest request) {
        FailureLogResponse response = failureLogService.logFailure(TEMP_USER_ID, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 4) 특정 목표의 실패 기록 조회 (필요 시)
     */
    @GetMapping
    public ResponseEntity<List<FailureLogResponse>> getFailureLogs(
            @RequestParam Long goalId
    ) {
        List<FailureLogResponse> logs = failureLogService.getFailureLogs(TEMP_USER_ID, goalId);
        return ResponseEntity.ok(logs);
    }
}

