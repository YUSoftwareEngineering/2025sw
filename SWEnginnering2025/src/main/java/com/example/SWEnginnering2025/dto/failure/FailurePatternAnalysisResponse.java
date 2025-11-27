/*Project: FailurePatternAnalysisResponse.java
        Author: 한지윤
        Date of creation: 2025.11.28
        Date of last update: 2025.11.28
 */

package com.example.SWEnginnering2025.dto.failure;

import lombok.Getter;
import java.util.Map;

/*
 실패 패턴 분석(요일/시간대별) 결과를 담는 DTO.
 countByWeekday      : 요일별 실패 횟수 집계 (예: "MONDAY" -> 3)
 countByTimeOfDay    : 시간대별 실패 횟수 집계 (예: "EVENING(18-22)" -> 5)
 mostFailedWeekday   : 가장 실패가 많이 발생한 요일 (동률이면 그 중 하나)
 mostFailedTimeOfDay : 가장 실패가 많이 발생한 시간대

 이 객체는 주로 AI 코칭 모듈(코칭 서비스)에서
 "어느 요일, 어느 시간대에 실패가 많이 몰려 있는지"를
 요약해서 사용하기 위한 용도이다.
 */
@Getter
public class FailurePatternAnalysisResponse {

    // 요일별 실패 횟수 ("MONDAY", "TUESDAY" ...)
    private final Map<String, Long> countByWeekday;

    // 시간대별 실패 횟수 ("DAWN(00-06)", "MORNING(06-12)" ...)
    private final Map<String, Long> countByTimeOfDay;

    // 가장 실패가 많이 일어난 요일 (없으면 null)
    private final String mostFailedWeekday;

    // 가장 실패가 많이 일어난 시간대 (없으면 null)
    private final String mostFailedTimeOfDay;

    public FailurePatternAnalysisResponse(
            Map<String, Long> countByWeekday,
            Map<String, Long> countByTimeOfDay,
            String mostFailedWeekday,
            String mostFailedTimeOfDay
    ) {
        this.countByWeekday = countByWeekday;
        this.countByTimeOfDay = countByTimeOfDay;
        this.mostFailedWeekday = mostFailedWeekday;
        this.mostFailedTimeOfDay = mostFailedTimeOfDay;
    }

}
