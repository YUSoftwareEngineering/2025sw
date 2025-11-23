package com.example.SWEnginnering2025.Controller;
import com.example.SWEnginnering2025.GoalService;
import com.example.SWEnginnering2025.dto.MonthlyCalendarDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.YearMonth;
/*
    Project: CalendarController.java
    Author: 윤나영
   Date of creation: 2025.11.23
   Date of last update: 2025.11.23
*/

// 캘린더 관련 API 요청을 처리하는 컨트롤러
@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

        private final GoalService goalService;

        public CalendarController(GoalService calendarService) {
            this.goalService = calendarService;
        }

        @GetMapping("/monthly")//월간 캘린더 조회 API
        public MonthlyCalendarDto getMonthlyCalendar(
                @RequestParam int year,
                @RequestParam int month
        ) {

            //month가 1~12를 벗어나는 경우 자동 보정 처리(ex: 25년 13월->26년 1월 )
            YearMonth ym = YearMonth.of(year, 1).plusMonths(month - 1);

            // // 보정된 연도와 월을 goal로 전달하여 월간 캘린더 생성
            return goalService.generateMonthlyCalendar(
                    ym.getYear(),
                    ym.getMonthValue()
            );
        }
    }

