package com.example.SWEnginnering2025.dto;
import java.util.List;
import lombok.Getter;
 /*
    Project: MonthlyCalendarDto.java
    Author: 윤나영
   Date of creation: 2025.11.23
   Date of last update: 2025.11.23
*/

//한 달 전체 캘린더 정보를 담아서 클라이언트에게 전달해준다
@Getter
public class MonthlyCalendarDto {

    private int year; //캘린더 연도
    private int month;//캘린더 달
    private List<DailyCalendarDto> days;// 해당 월의 모든 날짜 정보를 담고 있는 리스트
                                        // DailyCalendarDto에는 날짜(day), 요일, 목표 리스트가 들어 있음

    public MonthlyCalendarDto(int year, int month, List<DailyCalendarDto> days) {
        this.year = year;
        this.month = month;
        this.days = days;
    }
}

