/*Project: AttachmentController.java
        Author: 한지윤
        Date of creation: 2025.11.23
        Date of last update: 2025.11.23
                */


package com.example.SWEnginnering2025.service;

import com.example.SWEnginnering2025.domain.FailureLog;
import com.example.SWEnginnering2025.domain.FailureTag;
import com.example.SWEnginnering2025.dto.failure.CreateTagRequest;
import com.example.SWEnginnering2025.dto.failure.FailureLogResponse;
import com.example.SWEnginnering2025.dto.failure.FailureTagDto;
import com.example.SWEnginnering2025.dto.failure.LogFailureRequest;
import com.example.SWEnginnering2025.repository.FailureLogRepository;
import com.example.SWEnginnering2025.repository.FailureTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class FailureLogService {

    private final FailureTagRepository failureTagRepository;
    private final FailureLogRepository failureLogRepository;
    private final GoalService goalService; // 상태 FAILED로 바꾸는 용도

    public FailureLogService(FailureTagRepository failureTagRepository,
                             FailureLogRepository failureLogRepository,
                             GoalService goalService) {
        this.failureTagRepository = failureTagRepository;
        this.failureLogRepository = failureLogRepository;
        this.goalService = goalService;
    }

    // 1) 실패 기록 화면에서 사용자가 선택할 수 있는 태그 목록을 줌
    @Transactional(readOnly = true)
    public List<FailureTagDto> getFailureTags(Long userId) {
        //기본 제공 태그 + 사용자가 만든 태그 둘 다 가져옴
        List<FailureTag> tags = failureTagRepository.findByBuiltInTrueOrUserId(userId);
        //엔티티(FailureTag)를 클라이언트로 보내기 위한 DTO로 변환
        return tags.stream()
                .map(t -> new FailureTagDto(t.getId(), t.getName(), t.isBuiltIn()))
                .collect(toList());
    }

    // 2) 커스텀 태그 생성
    @Transactional
    public FailureTagDto createFailureTag(CreateTagRequest request) {
        //입력받은 태그 앞뒤 공백 제거
        String name = request.getName().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("태그 이름은 비어 있을 수 없습니다.");
        }

        //커스텀 태그 만들기
        var existing = failureTagRepository.findByUserIdAndName(request.getUserId(), name);
        //같은 유저가 같은 이름의 태그를 이미 만든 적 있는지 확인
        if (existing.isPresent()) {
            FailureTag tag = existing.get();
            //이미 있으면 기존 태그 그대로 돌려줌
            return new FailureTagDto(tag.getId(), tag.getName(), tag.isBuiltIn());
        }
        FailureTag tag = new FailureTag(request.getUserId(), name, false);
        FailureTag saved = failureTagRepository.save(tag);
        return new FailureTagDto(saved.getId(), saved.getName(), saved.isBuiltIn());
    }

    // 3) 실패 기록을 DB에 저장 + 해당 목표 상태를 FAILED로 바꿈
    @Transactional
    public FailureLogResponse logFailure(LogFailureRequest request) {
        //태그 없이 실패 로그 남길 경우 경고 메시지
        if (request.getTagIds() == null || request.getTagIds().isEmpty()) {
            throw new IllegalArgumentException("최소 1개 이상의 태그를 선택해야 합니다.");
        }
        //실패 로그에 연결할 태그들이 존재하는지 확인
        var tags = failureTagRepository.findAllById(request.getTagIds());
        if (tags.isEmpty()) {
            throw new IllegalArgumentException("선택한 태그를 찾을 수 없습니다.");
        }
        //실패 로그 생성
        FailureLog log = new FailureLog(
                request.getUserId(),
                request.getGoalId(),
                request.getDate(),
                request.getMemo(),
                LocalDateTime.now()
        );

        tags.forEach(log::addTag);
        //INSERT 실행 -> 저장된 FailureLog 엔티티 반환
        FailureLog saved = failureLogRepository.save(log);

        // 목표 상태를 FAILED로 변경 (GoalService 안 구현 필요)
        goalService.markGoalAsFailed(request.getGoalId());

        List<FailureTagDto> tagDtos = saved.getTags().stream()
                .map(t -> new FailureTagDto(t.getId(), t.getName(), t.isBuiltIn()))
                .collect(toList());

        return new FailureLogResponse(
                saved.getFailureId(),
                saved.getUserId(),
                saved.getGoalId(),
                saved.getFailedDate(),
                saved.getMemo(),
                saved.getFailedAt(),
                tagDtos
        );
    }
}
