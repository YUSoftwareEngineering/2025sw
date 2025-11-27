/*Project: CoachingController.java
        Author: 한지윤
        Date of creation: 2025.11.28
        Date of last update: 2025.11.28
 */

package com.example.SWEnginnering2025.controller;

import com.example.SWEnginnering2025.dto.coaching.AdviceRequest;
import com.example.SWEnginnering2025.dto.coaching.CoachingResultDto;
import com.example.SWEnginnering2025.service.CoachingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * AI 코칭 조언 관련 REST 컨트롤러.
 *
 * 클라이언트(React/Android 등)에서는
 *  GET 혹은 POST /api/v1/coaching/advice
 *  를 호출해서 개인 맞춤형 개선 조언을 받아간다.
 */
@RestController
@RequestMapping("/api/v1/coaching")
@RequiredArgsConstructor
public class CoachingController {

    private final CoachingService coachingService;

    /*
     * 개인 맞춤형 개선 조언 요청 엔드포인트.
     *
     * 예시 호출:
     *  POST /api/v1/coaching/advice
     *  {
     *      "userId": 1,
     *      "from": "2025-10-01",
     *      "to": "2025-11-01",
     *      "tone": "COACH",
     *      "maxItems": 3
     *  }
     */
    @PostMapping("/advice")
    public ResponseEntity<CoachingResultDto> getAdvice(@RequestBody AdviceRequest request) {
        CoachingResultDto result = coachingService.getAdvice(request);
        return ResponseEntity.ok(result);
    }
}
