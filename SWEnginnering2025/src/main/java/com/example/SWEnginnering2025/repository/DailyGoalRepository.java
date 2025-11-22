package com.example.SWEnginnering2025.repository;

import com.example.SWEnginnering2025.domain.DailyGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyGoalRepository extends JpaRepository<DailyGoal, Long> {
}