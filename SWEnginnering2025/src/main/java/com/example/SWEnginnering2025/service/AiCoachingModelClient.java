/*Project: AiCoachingModelClient.java
        Author: 한지윤
        Date of creation: 2025.11.28
        Date of last update: 2025.11.28
 */

package com.example.SWEnginnering2025.service;

import com.example.SWEnginnering2025.dto.coaching.CoachingResultDto;

/*
 * 외부 LLM(AI 코치)와의 연동을 담당하는 인터페이스.
 *
 * 실제 구현체는 나중에 OpenAI / Gemini / 자체 Python 서버 등으로 교체 가능하다.
 * 지금은 SDS의 AiCoachingModel 역할을 자바 코드 상에서 표현하는 용도.
 */
public interface AiCoachingModelClient {

    /*
     * 이미 서버에서 분석한 데이터를 기반으로
     * LLM에게 코칭 메시지 생성을 요청한다.
     *
     * @param prompt LLM에게 전달할 최종 프롬프트(한글/영문 자유)
     * @return CoachingResultDto AI가 생성한 조언을 앱에서 쓰기 좋은 구조로 가공한 결과
     */
    CoachingResultDto generateCoachingAdvice(String prompt);
}
