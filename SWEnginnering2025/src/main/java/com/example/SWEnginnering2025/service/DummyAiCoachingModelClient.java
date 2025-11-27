/*Project: DummyAiCoachingModelClient.java
        Author: 한지윤
        Date of creation: 2025.11.28
        Date of last update: 2025.11.28
 */

package com.example.SWEnginnering2025.service;

import com.example.SWEnginnering2025.dto.coaching.CoachingResultDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/*
 * 실제 LLM이 아직 연결되지 않은 상태에서 사용하는 더미 구현.
 * - 프롬프트를 그대로 로그/메타 정보로 넣고,
 * - 간단한 고정 메시지를 반환한다.
 *
 * 나중에 Python(FastAPI)나 OpenAI SDK를 쓰는 실제 구현체로 대체하면 된다.
 */
@Component
public class DummyAiCoachingModelClient implements AiCoachingModelClient {

    @Override
    public CoachingResultDto generateCoachingAdvice(String prompt) {

        // TODO: 실제로는 prompt를 외부 LLM 서버에 보내고 응답을 파싱해야 한다.

        return CoachingResultDto.builder()
                .weakestPoints(Map.of(
                        "weekday", "MONDAY",
                        "timeOfDay", "EVENING(18-22)"
                ))
                .adviceMessage(
                        "최근 몇 주 동안 월요일 저녁에 실패가 자주 발생하고 있어요. " +
                                "이 시간대를 '집중 루틴'으로 고정해 보고, 작은 단위의 계획부터 다시 세워보세요."
                )
                .recommendations(List.of(
                        "월요일 18시에 25분 포커스 타이머를 예약해 두세요.",
                        "실패 사유 태그 중 '시간관리 실패'에 주 1회 회고 시간을 만들어 보세요."
                ))
                .meta(Map.of(
                        "promptPreview", prompt.substring(0, Math.min(prompt.length(), 200)),
                        "llmProvider", "Dummy"
                ))
                .build();
    }
}
