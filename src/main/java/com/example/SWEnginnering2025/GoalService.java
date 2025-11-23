package com.example.SWEnginnering2025;
import com.example.SWEnginnering2025.dto.DailyCalendarDto;
import com.example.SWEnginnering2025.dto.MonthlyCalendarDto;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
 /*
    Project: GoalService.java
    Author: 윤나영
   Date of creation: 2025.11.23
   Date of last update: 2025.11.23
*/


/**
 * 특정 year와 month에 해당하는 월간 캘린더 데이터를 생성하여 반환한다.
 * DB에서 목표(goal) 정보를 불러오는 부분은 예시 목표로 대체하였고, 추후에 실제 DB 연동이 가능하도록 확장할 수 있다.
 */

@Service
public class GoalService {
    public MonthlyCalendarDto generateMonthlyCalendar(int year, int month) {
        List<DailyCalendarDto> days = new ArrayList<>();//각 day(일)의 정보를 담을 리스트(1~30혹은 31일)
        YearMonth ym = YearMonth.of(year, month);//year,month값 기반으로 YearMonth객테 생성

        for (int day = 1; day <= ym.lengthOfMonth(); day++) {//각 월의 1일 부터 마지막 일까지 반목
            LocalDate date = ym.atDay(day);//현재 day값을 2025-11-23 식으로 변환
            String dow = date.getDayOfWeek().toString();//요일 값을 문자열로 반환

            List<String> goals = List.of("예시 목표 1", "예시 목표 2");//실제 목표는 DB에서 가져오도록 변경필요

            days.add(new DailyCalendarDto(day, dow, goals));//하루에 대한 정보를 DTO형태로 리스트 추가
        }

        return new MonthlyCalendarDto(year, month, days);//월 전체 정보를 담은 DTO로 반환
    }
}
