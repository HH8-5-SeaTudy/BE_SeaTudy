package com.finalproject.seatudy.timeCheck.repository;

import com.finalproject.seatudy.dto.login.Member;
import com.finalproject.seatudy.timeCheck.entity.TimeCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeCheckRepository extends JpaRepository<TimeCheck, Long> {
    List<TimeCheck> findByMemberAndDate(Member member, String date);
}
