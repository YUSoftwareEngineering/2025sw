package com.example.SWEnginnering2025.dto;
import java.util.List;
import lombok.Getter;
/*
    Project: DailyCalendarDto.java
    Author: 윤나영
   Date of creation: 2025.11.23
   Date of last update: 2025.11.23
*/

@Getter
public class DailyCalendarDto {
    // 한 일자에 대한 정보 저장하는 DTO

        private int day;//해당날짜 일
        private String dayOfWeek;//해당 날짜 요일
        private List<String> goals;//해당낭짜 목표

        //생성자
        public DailyCalendarDto(int day, String dayOfWeek, List<String> goals) {
            this.day = day;
            this.dayOfWeek = dayOfWeek;
            this.goals = goals;
        }
    }

