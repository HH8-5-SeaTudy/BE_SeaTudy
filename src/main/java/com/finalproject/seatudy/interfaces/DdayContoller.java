package com.finalproject.seatudy.interfaces;

import com.finalproject.seatudy.service.dto.request.DdayRequestDto;
import com.finalproject.seatudy.service.dto.response.DdayResponseDto;
import com.finalproject.seatudy.service.DdayService;
import com.finalproject.seatudy.security.UserDetailsImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class DdayContoller {

    private final DdayService ddayService;

    @PostMapping("/ddays")
    @ApiOperation(value = "D-day 생성")
    public DdayResponseDto createDday(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestBody DdayRequestDto requestDto) throws ParseException {
        return ddayService.createDday(userDetails, requestDto);
    }

    @GetMapping("/ddays")
    @ApiOperation(value = "D-day 조회")
    public List<DdayResponseDto> getAllDday(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ddayService.getAllDday(userDetails);
    }

    @PutMapping("/ddays/{ddayId}")
    @ApiOperation(value = "D-day 수정")
    @ApiImplicitParam(name = "ddayId", value = "D-day 아이디")
    public DdayResponseDto updateDday(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable Long ddayId,
                                      @RequestBody DdayRequestDto requestDto) throws ParseException{
        return ddayService.updateDday(userDetails, ddayId, requestDto);
    }

    @DeleteMapping("/ddays/{ddayId}")
    @ApiOperation(value = "D-day 삭제")
    @ApiImplicitParam(name = "ddayId", value = "D-day 아이디")
    public String deleteDday(@AuthenticationPrincipal UserDetailsImpl userDetails,
                            @PathVariable Long ddayId){
        return ddayService.deleteDday(userDetails, ddayId);
    }
}
