/*Project: AttachmentController.java
        Author: 한지윤
        Date of creation: 2025.11.23
        Date of last update: 2025.11.23
                */


package com.example.SWEnginnering2025.service;

import com.example.SWEnginnering2025.dto.failure.FailurePatternAnalysisResponse;

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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    /*실패 패턴 분석
    사용자의 실패 로그를 가져와서
    시간대, 요일별 실패 횟수를 카운터하고
    가장 실패가 많이 몰린 요일과 시간대를 계산
     */
    @Transactional(readOnly = true)
    public FailurePatternAnalysisResponse analyzeFailurePatternByWeekdayAndTime(
            Long userId,
            LocalDate from,
            LocalDate to
    ) {
        // 1) 날짜 범위 기본값 처리
        //    - from/to 가 null로 들어오면, 전체 기간 / 오늘 기준으로 보정
        if (from == null) {
            // 가장 과거까지 보고 싶다면, 굉장히 옛날 날짜로 설정
            from = LocalDate.of(1970, 1, 1);
        }
        if (to == null) {
            // to 가 없으면 오늘까지 분석
            to = LocalDate.now();
        }

        // 2) 해당 유저의 기간 내 실패 로그 조회
        //    이미 Repository에 정의된 메서드:
        //    List<FailureLog> findByUserIdAndFailedDateBetween(Long userId, LocalDate from, LocalDate to);
        List<FailureLog> logs = failureLogRepository.findByUserIdAndFailedDateBetween(userId, from, to);

        // 3) 요일별, 시간대별 카운트를 저장할 맵 초기화
        //    - EnumMap: DayOfWeek 를 key로 쓰기 위해 사용 (메모리 효율 + 성능)
        Map<DayOfWeek, Long> weekdayCount = new EnumMap<>(DayOfWeek.class);

        //    - 시간대는 간단하게 5구간으로 나눈다.
        //      00~05: 새벽, 06~11: 오전, 12~17: 오후, 18~21: 저녁, 22~23: 밤
        Map<String, Long> timeOfDayCount = new LinkedHashMap<>();
        timeOfDayCount.put("DAWN(00-06)", 0L);       // 새벽
        timeOfDayCount.put("MORNING(06-12)", 0L);   // 오전
        timeOfDayCount.put("AFTERNOON(12-18)", 0L); // 오후
        timeOfDayCount.put("EVENING(18-22)", 0L);   // 저녁
        timeOfDayCount.put("NIGHT(22-24)", 0L);     // 밤

        // 4) 각 실패 로그를 순회하면서 요일/시간대별로 카운트 증가
        for (FailureLog log : logs) {
            // (1) 실패 시각을 가져온다.
            //     - failedAt 이 null 인 경우를 대비해서, 실패 날짜의 00:00 으로 보정한다.
            LocalDateTime failedAt = log.getFailedAt();
            if (failedAt == null) {
                LocalDate failedDate = log.getFailedDate();
                if (failedDate != null) {
                    failedAt = failedDate.atStartOfDay();
                } else {
                    // 날짜 정보도 없다면 분석에서 제외 (안전장치)
                    continue;
                }
            }

            // (2) 요일 계산 (월~일)
            DayOfWeek dayOfWeek = failedAt.getDayOfWeek();
            weekdayCount.merge(dayOfWeek, 1L, Long::sum);

            // (3) 시간대 계산 (hour 기준으로 구간 나누기)
            LocalTime time = failedAt.toLocalTime();
            int hour = time.getHour(); // 0~23

            String slotKey;
            if (hour < 6) {
                slotKey = "DAWN(00-06)";
            } else if (hour < 12) {
                slotKey = "MORNING(06-12)";
            } else if (hour < 18) {
                slotKey = "AFTERNOON(12-18)";
            } else if (hour < 22) {
                slotKey = "EVENING(18-22)";
            } else {
                slotKey = "NIGHT(22-24)";
            }

            timeOfDayCount.merge(slotKey, 1L, Long::sum);
        }

        // 5) 요일별 카운트를 문자열 Key 로 변환
        //    - Enum(DayOfWeek) 그대로 내려보내도 되지만,
        //      클라이언트(앱) 입장에서 단순 문자열이 다루기 편하므로 변환한다.
        Map<String, Long> weekdayResult = new LinkedHashMap<>();
        for (DayOfWeek d : DayOfWeek.values()) {
            long count = weekdayCount.getOrDefault(d, 0L);
            // d.name() 은 "MONDAY" 같은 대문자 영문 문자열
            weekdayResult.put(d.name(), count);
        }

        // 6) 최다 실패 요일 / 시간대 계산
        String mostFailedWeekday = null;
        long maxWeekdayCount = 0L;
        for (Map.Entry<String, Long> entry : weekdayResult.entrySet()) {
            if (entry.getValue() > maxWeekdayCount) {
                maxWeekdayCount = entry.getValue();
                mostFailedWeekday = entry.getKey();
            }
        }

        String mostFailedTimeOfDay = null;
        long maxTimeSlotCount = 0L;
        for (Map.Entry<String, Long> entry : timeOfDayCount.entrySet()) {
            if (entry.getValue() > maxTimeSlotCount) {
                maxTimeSlotCount = entry.getValue();
                mostFailedTimeOfDay = entry.getKey();
            }
        }

        // 7) DTO로 묶어서 반환
        return new FailurePatternAnalysisResponse(
                weekdayResult,
                timeOfDayCount,
                mostFailedWeekday,
                mostFailedTimeOfDay
        );
    }
}
