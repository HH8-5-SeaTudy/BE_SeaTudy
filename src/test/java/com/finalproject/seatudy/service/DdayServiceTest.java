package com.finalproject.seatudy.service;

import com.finalproject.seatudy.domain.entity.Dday;
import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.repository.DdayRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.dto.request.DdayRequestDto;
import com.finalproject.seatudy.service.dto.response.DdayResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DdayServiceTest {
    @InjectMocks
    private DdayService ddayService;
    @Mock
    private DdayRepository ddayRepository;

    @Test
    @DisplayName("Dday 등록 테스트")
    void createDday() throws ParseException {
        //given
        Member member = Member.builder()
                .memberId(1L)
                .email("user@naver.com")
                .password("user12345")
                .build();

        DdayRequestDto requestDto = DdayRequestDto.builder()
                .title("항해99 5조 회의")
                .targetDay("2022-09-24")
                .build();

        //stub
        UserDetailsImpl userDetails = new UserDetailsImpl(member);
//        when(ddayRepository.save(any())).thenReturn(dday);

        //when
        DdayResponseDto responseDto = ddayService.createDday(userDetails,requestDto);

        //then
        assertEquals(responseDto.getTitle(), requestDto.getTitle());
        assertEquals(responseDto.getTargetDay(), requestDto.getTargetDay());
        assertEquals(responseDto.getDday(), 10);
    }

    @Test
    @DisplayName("Dday 조회 테스트")
    void getDday() {
        //given
        Member member = Member.builder()
                .memberId(1L)
                .email("user@naver.com")
                .password("user12345")
                .build();

        List<Dday> ddays = new ArrayList<>();
        ddays.add(Dday.builder()
                .ddayId(1L)
                .title("항해99 5조 오전회의")
                .targetDay("2022-09-24")
                .dday(10L)
                .member(member)
                .build());
        ddays.add(Dday.builder()
                .ddayId(2L)
                .title("항해99 5조 오후회의")
                .targetDay("2022-09-29")
                .dday(15L)
                .member(member)
                .build());

        //stub
        UserDetailsImpl userDetails = new UserDetailsImpl(member);
        when(ddayRepository.findAllByMember(member)).thenReturn(ddays);

        //when
        List<DdayResponseDto> responseDtos = ddayService.getAllDday(userDetails);

        //then
        assertEquals(responseDtos.get(0).getDdayId(), ddays.get(0).getDdayId());
        assertEquals(responseDtos.get(0).getTitle(), ddays.get(0).getTitle());
        assertEquals(responseDtos.get(0).getTargetDay(), ddays.get(0).getTargetDay());
        assertEquals(responseDtos.get(0).getDday(), ddays.get(0).getDday());
        assertEquals(responseDtos.get(1).getDdayId(), ddays.get(1).getDdayId());
        assertEquals(responseDtos.get(1).getTitle(), ddays.get(1).getTitle());
        assertEquals(responseDtos.get(1).getTargetDay(), ddays.get(1).getTargetDay());
        assertEquals(responseDtos.get(1).getDday(), ddays.get(1).getDday());
        assertEquals(responseDtos.size(), ddays.size());
    }

    @Test
    @DisplayName("Dday 수정 테스트")
    void updateDday() throws ParseException {
        //given
        Member member = Member.builder()
                .memberId(1L)
                .email("user@naver.com")
                .password("user12345")
                .build();

        DdayRequestDto requestDto = DdayRequestDto.builder()
                .title("항해99 5조 오후회의")
                .targetDay("2022-09-29")
                .build();

        //stub
        UserDetailsImpl userDetails = new UserDetailsImpl(member);

        Dday dday = Dday.builder()
                .ddayId(1L)
                .title("항해99 5조 오전 회의")
                .targetDay("2022-09-24")
                .dday(10L)
                .member(member)
                .build();
        when(ddayRepository.findById(dday.getDdayId())).thenReturn(Optional.of(dday));

        //when
        DdayResponseDto responseDto = ddayService.updateDday(userDetails, dday.getDdayId(), requestDto);

        //then
        assertEquals(responseDto.getDdayId(), dday.getDdayId());
        assertEquals(responseDto.getTitle(), requestDto.getTitle());
        assertEquals(responseDto.getTargetDay(), responseDto.getTargetDay());
        assertEquals(responseDto.getDday(), 15);
    }

    @Test
    @DisplayName("Dday 삭제 테스트")
    void deleteDday() {
        //given
        Member member = Member.builder()
                .memberId(1L)
                .email("user@naver.com")
                .password("user12345")
                .build();

        Dday dday = Dday.builder()
                .ddayId(1L)
                .title("항해99 5조 오전 회의")
                .targetDay("2022-09-24")
                .dday(10L)
                .member(member)
                .build();

        //stub
        UserDetailsImpl userDetails = new UserDetailsImpl(member);
        when(ddayRepository.findById(dday.getDdayId())).thenReturn(Optional.of(dday));

        //when
        String deleteDday = ddayService.deleteDday(userDetails, dday.getDdayId());

        //then
        assertEquals(deleteDday, "삭제되었습니다.");
    }
}