package com.finalproject.seatudy.service;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.Rank;
import com.finalproject.seatudy.domain.entity.TimeCheck;
import com.finalproject.seatudy.domain.repository.RankRepository;
import com.finalproject.seatudy.domain.repository.TimeCheckRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.dto.response.TimeCheckListDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.Format;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.finalproject.seatudy.service.util.Formatter.stf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeCheckServiceTest {
    @InjectMocks
    private TimeCheckService timeCheckService;
    @Mock
    private TimeCheckRepository timeCheckRepository;
    @Mock
    private RankRepository rankRepository;

    @Nested
    @DisplayName("체크인 기능 테스트")
    class chckIn {

        @Test
        @DisplayName("체크인을 처음 했을 때")
        void checkInFirst() throws ParseException {
            //given
            Member member = Member.builder()
                    .memberId(1L)
                    .email("user@naver.com")
                    .password("user12345")
                    .build();

            //stub
            UserDetailsImpl userDetails = new UserDetailsImpl(member);

            //when
            TimeCheckListDto.CheckIn checkIn = timeCheckService.checkIn(userDetails);

            //then
            assertEquals(checkIn.getCheckIn(), LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            assertEquals(checkIn.getTimeWatch(), "00:00:00");
        }

        @Test
        @DisplayName("체크인 기록이 여러번 있을 때")
        void checkInMany() throws ParseException {
            //given
            Member member = Member.builder()
                    .memberId(1L)
                    .email("user@naver.com")
                    .password("user12345")
                    .build();

            List<TimeCheck> timeChecks = new ArrayList<>();
            timeChecks.add(TimeCheck.builder()
                    .checkId(1L)
                    .checkIn("18:15:00")
                    .checkOut("19:30:30")
                    .date(LocalDate.now().toString())
                    .member(member)
                    .build());
            timeChecks.add(TimeCheck.builder()
                    .checkId(2L)
                    .checkIn("19:45:00")
                    .checkOut("20:00:00")
                    .date(LocalDate.now().toString())
                    .member(member)
                    .build());

            Rank rank = Rank.builder()
                    .rankId(1L)
                    .dayStudy("01:30:30")
                    .date(LocalDate.now().toString())
                    .member(member)
                    .timeChecks(timeChecks)
                    .build();

            //stub
            UserDetailsImpl userDetails = new UserDetailsImpl(member);
            when(rankRepository.findByMemberAndDate(member, LocalDate.now().toString())).thenReturn(Optional.of(rank));

            //when
            TimeCheckListDto.CheckIn checkIn = timeCheckService.checkIn(userDetails);

            //then
            assertEquals(checkIn.getCheckIn(), LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            assertEquals(checkIn.getTimeWatch(), rank.getDayStudy());
        }
    }

    @Nested
    @DisplayName("체크아웃 테스트")
    class checkOut {

        @Test
        @DisplayName("체크아웃을 처음했을 때")
        void checkoutFirst() throws ParseException {
            //given
            Member member = Member.builder()
                    .memberId(1L)
                    .email("user@naver.com")
                    .password("user12345")
                    .build();

            TimeCheck timeCheck = TimeCheck.builder()
                    .checkIn("20:00:00")
                    .date(LocalDate.now().toString())
                    .member(member)
                    .build();

            //stub
            UserDetailsImpl userDetails = new UserDetailsImpl(member);
            when(timeCheckRepository.findByMemberAndDate(member, LocalDate.now().toString())).thenReturn(Collections.singletonList(timeCheck));

            //when
            TimeCheckListDto.CheckOut checkOut = timeCheckService.checkOut(userDetails);

            //then
            assertEquals(checkOut.getCheckOut(), LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            assertEquals(checkOut.getTimeWatch(), "03:00:00");
        }

        @Test
        @DisplayName("체크아웃 기록이 여러번 있을 때")
        void checkOut() throws ParseException {
            //given
            Member member = Member.builder()
                    .memberId(1L)
                    .email("user@naver.com")
                    .password("user12345")
                    .build();

            List<TimeCheck> timeChecks = new ArrayList<>();
            timeChecks.add(TimeCheck.builder()
                    .checkId(1L)
                    .checkIn("21:00:00")
                    .checkOut("22:00:00")
                    .date(LocalDate.now().toString())
                    .member(member)
                    .build());
            timeChecks.add(TimeCheck.builder()
                    .checkId(2L)
                    .checkIn("23:00:00")
                    .date(LocalDate.now().toString())
                    .member(member)
                    .build());

            Rank rank = Rank.builder()
                    .rankId(1L)
                    .dayStudy("01:00:00")
                    .date(LocalDate.now().toString())
                    .member(member)
                    .timeChecks(timeChecks)
                    .build();

            //stub
            UserDetailsImpl userDetails = new UserDetailsImpl(member);
            when(rankRepository.findByMemberAndDate(member, LocalDate.now().toString())).thenReturn(Optional.of(rank));
            when(timeCheckRepository.findByMemberAndDate(member, LocalDate.now().toString())).thenReturn(Collections.singletonList(timeChecks.get(timeChecks.size() - 1)));

            //when
            TimeCheckListDto.CheckOut checkOut = timeCheckService.checkOut(userDetails);

            //then
            assertEquals(checkOut.getCheckOut(), LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            assertEquals(checkOut.getTimeWatch(), "01:52:00");
        }

        @Test
        @DisplayName("시간 조회")
        void getCheckIn() throws ParseException {
            //given
            Member member = Member.builder()
                    .memberId(1L)
                    .email("user@naver.com")
                    .password("user12345")
                    .build();

            List<TimeCheck> timeChecks = new ArrayList<>();
            timeChecks.add(TimeCheck.builder()
                    .checkId(1L)
                    .checkIn("19:30:00")
                    .checkOut("20:30:00")
                    .date(LocalDate.now().toString())
                    .member(member)
                    .build());
            timeChecks.add(TimeCheck.builder()
                    .checkId(2L)
                    .checkIn("21:00:00")
                    .checkOut("21:15:30")
                    .date(LocalDate.now().toString())
                    .member(member)
                    .build());

            List<Rank> ranks = new ArrayList<>();
            ranks.add(Rank.builder()
                    .rankId(1L)
                    .dayStudy("01:15:30")
                    .date(LocalDate.now().toString())
                    .member(member)
                    .timeChecks(timeChecks)
                    .build());
            ranks.add(Rank.builder()
                    .rankId(2L)
                    .dayStudy("01:00:00")
                    .date("2022-09-14")
                    .member(member)
                    .build());

            //stub
            UserDetailsImpl userDetails = new UserDetailsImpl(member);
            when(rankRepository.findByMemberAndDate(member, LocalDate.now().toString())).thenReturn(Optional.of(ranks.get(0)));
            when(timeCheckRepository.findByMemberAndDate(member, LocalDate.now().toString())).thenReturn(timeChecks);
            when(rankRepository.findByMember(member)).thenReturn(ranks);

            //when
            TimeCheckListDto.TimeCheckDto timeCheckDto = timeCheckService.getCheckIn(userDetails);

            //then
            assertEquals(timeCheckDto.getDayStudyTime(), ranks.get(0).getDayStudy());
            assertEquals(timeCheckDto.getTotalStudyTime(), "02:15:30");
//            assertEquals(timeCheckDto.getTodayLogs(), "");
            timeCheckDto.getTodayLogs().stream().forEach(todayLogDto -> System.out.println(todayLogDto));
        }
    }
}