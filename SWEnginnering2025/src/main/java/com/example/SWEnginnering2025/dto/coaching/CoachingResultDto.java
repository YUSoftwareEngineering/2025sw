/*Project: CoachingResultDto.java
        Author: 한지윤
        Date of creation: 2025.11.28
        Date of last update: 2025.11.28
 */

package com.example.SWEnginnering2025.dto.coaching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/*
 * AI 코칭 조언 결과 DTO.
 *
 * SRS 24.1.1 ~ 24.1.5 내용을 코드로 옮긴 구조:
 * - weakestPoints  : 가장 취약한 요일/시간대/카테고리 요약
 * - adviceMessage  : LLM이 생성한 자연어 코칭 메시지 전체
 * - recommendations: 추천 루틴/행동 리스트
 * - meta           : 기반이 된 통계/점수 등을 간단히 담을 수 있는 메타데이터
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachingResultDto {

    /*
     * 예:
     *  - "weekday"   -> "MONDAY"
     *  - "timeOfDay" -> "EVENING(18-22)"
     *  - "category"  -> "시간관리"
     */
    private Map<String, String> weakestPoints;

    /*
     * AI가 생성한 메인 코칭 메시지(문단 형태).
     * 예: "월요일 저녁에 반복적으로 시간을 넘기고 있어요. 다음 주부터는 ..."
     */
    private String adviceMessage;

    /*
     * 행동 가이드/추천 루틴 등 세부 조언 리스트.
     * 예: ["월요일 18시에 25분 타이머 루틴을 등록해 보세요.", "실패 태그 '시간관리 실패'를 집중 점검해 보세요."]
     */
    private List<String> recommendations;

    /*
     * - "successRate"      -> "72%"
     * - "totalFailures"    -> "15"
     * - "focusScore"       -> "3.4"
     * 같은 통계/스코어를 문자열로 간단히 담을 수 있는 용도.
     */
    private Map<String, String> meta;
}
