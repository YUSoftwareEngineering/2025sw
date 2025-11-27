/*Project: CoachingService.java
        Author: 한지윤
        Date of creation: 2025.11.28
        Date of last update: 2025.11.28
 */

package com.example.SWEnginnering2025.service;

import com.example.SWEnginnering2025.dto.coaching.AdviceRequest;
import com.example.SWEnginnering2025.dto.coaching.CoachingResultDto;
import com.example.SWEnginnering2025.dto.failure.FailurePatternAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/*
 * 개인 맞춤형 개선 조언(Use case: Get AI Advice)을 생성하는 핵심 서비스.
 *
 * 흐름:
 *  1) FailureLogService에서 실패 패턴 분석 결과를 가져온다. (SRS 23번)
 *  2) 필요하다면 GoalService 등에서 성공률/집중도 등의 요약 데이터를 가져온다.
 *  3) 위 정보를 한글 텍스트 프롬프트로 정리한다.
 *  4) AiCoachingModelClient(LLM 클라이언트)에 프롬프트를 보내고 결과를 받는다.
 *  5) 필요 시, 생성된 조언을 LogDB에 저장한다. (지금은 TODO)
 */
@Service
@RequiredArgsConstructor
public class CoachingService {

    private final FailureLogService failureLogService;
    private final AiCoachingModelClient aiCoachingModelClient;
    // private final GoalService goalService;  // 추후 성공/실패 통계까지 포함하고 싶으면 주입

    /*
     * 개인 맞춤형 개선 조언 생성.
     *
     * @param request AdviceRequest (userId, 기간, 톤 등)
     * @return CoachingResultDto  화면에 바로 쓸 수 있는 조언 결과
     */
    @Transactional(readOnly = true)
    public CoachingResultDto getAdvice(AdviceRequest request) {

        Long userId = request.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("userId는 필수입니다.");
        }

        // 1) 분석 기간 보정: from/to 가 비어 있으면 최근 4주 ~ 오늘로 설정
        LocalDate to = request.getTo() != null ? request.getTo() : LocalDate.now();
        LocalDate from = request.getFrom() != null ? request.getFrom() : to.minusWeeks(4);

        // 2) 실패 패턴 분석 결과 조회 (요일/시간대)
        FailurePatternAnalysisResponse pattern =
                failureLogService.analyzeFailurePatternByWeekdayAndTime(userId, from, to);

        // 3) 프롬프트 문자열 생성
        String prompt = buildPromptForLLM(request, pattern);

        // 4) LLM(AI 코치)에게 코칭 메시지 요청
        CoachingResultDto rawAdvice = aiCoachingModelClient.generateCoachingAdvice(prompt);

        // 5) TODO: 생성된 조언을 LogDB에 저장하는 로직 추가 (Save AI Advice use case)
        //    ex) adviceLogRepository.save(...);

        return rawAdvice;
    }

    /*
     * LLM에 넘길 프롬프트를 한글 텍스트로 구성하는 헬퍼 메서드.
     * SDS 4.2.10 + SRS 24.1.1 ~ 24.1.5 요구사항을 만족하도록
     * 실패 패턴, 취약 요일/시간대, 카테고리 등을 설명해 준다.
     */
    private String buildPromptForLLM(AdviceRequest request,
                                     FailurePatternAnalysisResponse pattern) {

        String tone = request.getTone() != null ? request.getTone() : "COACH";
        String mostWeekday = pattern.getMostFailedWeekday();
        String mostTimeOfDay = pattern.getMostFailedTimeOfDay();

        StringBuilder sb = new StringBuilder();

        sb.append("당신은 사용자의 자기관리 실패 패턴을 분석해서, ")
                .append("데이터에 근거한 코칭 메시지를 제공하는 전문 코치 AI입니다.\n\n");

        sb.append("요구 톤(tone): ").append(tone).append("\n");
        sb.append("출력 형식: 1) 한 문단 이상의 메인 조언, 2) 실천 가능한 행동 가이드 3~5개.\n\n");

        sb.append("아래는 최근 실패 패턴 분석 결과입니다.\n");
        sb.append("- 분석 기간: ").append(request.getFrom()).append(" ~ ").append(request.getTo()).append("\n");
        sb.append("- 가장 실패가 많이 발생한 요일: ").append(mostWeekday).append("\n");
        sb.append("- 가장 실패가 많이 발생한 시간대: ").append(mostTimeOfDay).append("\n\n");

        sb.append("요일별 실패 횟수:\n");
        pattern.getCountByWeekday().forEach((k, v) ->
                sb.append("  * ").append(k).append(": ").append(v).append("회\n")
        );

        sb.append("\n시간대별 실패 횟수:\n");
        pattern.getCountByTimeOfDay().forEach((k, v) ->
                sb.append("  * ").append(k).append(": ").append(v).append("회\n")
        );

        sb.append("\n위 정보를 바탕으로, ");
        sb.append("사용자에게 가장 취약한 패턴(요일, 시간대, 습관 유형)을 알려주고, ");
        sb.append("다음 주부터 바로 실천할 수 있는 구체적인 루틴/행동 전략을 제안해 주세요.\n");
        sb.append("조언은 비난이 아니라 성장과 개선을 돕는 방향으로 작성해 주세요.");

        return sb.toString();
    }
}
