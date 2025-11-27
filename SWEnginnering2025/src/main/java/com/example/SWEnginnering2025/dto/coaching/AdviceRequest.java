/*Project: AdviceRequest.java
        Author: 한지윤
        Date of creation: 2025.11.28
        Date of last update: 2025.11.28
 */

package com.example.SWEnginnering2025.dto.coaching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/*
 * AI 코칭 조언 요청 DTO.
 *
 * - userId        : 조언을 요청한 사용자 ID
 * - from          : 분석 시작 날짜 (예: 최근 4주)
 * - to            : 분석 종료 날짜 (null 이면 오늘까지)
 * - tone          : AI 톤(예: "COACH", "CHEER_UP") - 선택값
 * - maxItems      : 추천 루틴/포인트 개수 상한
 *
 * SRS 24번: 개인 맞춤형 개선 조언 생성을 위한
 * 서버→AI 모듈 호출 시 사용되는 최소한의 입력 데이터 구조.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdviceRequest {

    private Long userId;

    private LocalDate from;
    private LocalDate to;

    // ex) "COACH", "CHEER_UP", "STRICT"
    private String tone;

    // ex) 3 → 가장 취약한 포인트 TOP3 정도만 정리
    private Integer maxItems;
}
