package com.finalproject.seatudy.interfaces;

import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.dto.response.TimeCheckListDto;
import com.finalproject.seatudy.service.TimeCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TimeCheckController {

    private final TimeCheckService timeCheckService;

    // 체크인 start
    @PostMapping("/checkIns")
    public TimeCheckListDto.CheckIn checkIn(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException{
        log.info("요청 메서드 [POST] /api/v1/checkIns");
        return timeCheckService.checkIn(userDetails);
    }

    // 로그인된 사용자가 이미 start를 누른 상태라면, 값을 내려준다.
    @GetMapping("/checkIns")
    public TimeCheckListDto.TimeCheckDto getCheckIn(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException{
        log.info("요청 메서드 [GET] /api/checkIn");
        return timeCheckService.getCheckIn(userDetails);
    }

    @PostMapping("/checkOuts")
    public TimeCheckListDto.CheckOut checkOut(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException{
        log.info("요청 메서드 [POST] /api/checkOut");
        return timeCheckService.checkOut(userDetails);
    }
}
