package com.finalproject.seatudy.service.util;

import com.finalproject.seatudy.domain.entity.Rank;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.finalproject.seatudy.service.util.Formatter.*;
import static java.lang.String.format;

public class CalendarUtil {

    // 캘린더 생성
    // SimpleDateFormat.parse()메소드는 입력한 문자열 형식의 날짜가 Format이 다를경우 java.text.ParseException을 발생한다.
    public static Calendar todayCalendar(String date) throws ParseException{
        Calendar today = Calendar.getInstance(); // 캘린더를 생성 (캘린더는 추상클래스이므로 getInstance()을 통해 구현된 객체를 얻어야함)
        today.setTime(sdf.parse(date)); // 파라미터 기준으로 캘린더 셋팅
        return today;
    }

    // 날짜 형식에 맞게 string으로 포맷
    public static String dateFormat(Calendar calendar){
        return sdf.format(calendar.getTime());
    }

    // 현재 시간 기준으로 셋팅
    public static void todayCalendarTime(Calendar setTime) throws ParseException{
        String nowDateTime = nowDateTime(); // 현재 시간 -> string yyyy-MM-dd HH:mm:ss 형식
        Date nowFormatter = sdtf.parse(nowDateTime); // DateTime 형식으로 포맷
        setTime.setTime(nowFormatter);
    }

    // 당일 오전 다섯시로 셋팅
    public static void setCalendarTime(Calendar setTime) throws ParseException {
        String strToday = dateFormat(setTime); //오늘 날짜 str yyyy-MM-dd 형식
        Date setFormatter = dateTimeFormat(strToday); //yyyy-MM-dd 05:00:00(당일 오전 5시)
        setTime.setTime(setFormatter); //yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용
    }

    public static String totalTime(List<Rank> allUserList) {

        int total = 0; //총 누적 공부시간이 담김

        for (Rank find : allUserList) {
            String[] arrayFind = find.getDayStudy().split(":");
            int allHH = Integer.parseInt(arrayFind[0]);
            int allMM = Integer.parseInt(arrayFind[1]);
            int allSS = allHH * 3600 + allMM * 60;
            int alltotal = allSS + Integer.parseInt(arrayFind[2]); //초으로 환산
            total += alltotal;
        }

        int totalHH = (total / 3600);
        int totalMM = ((total % 3600) / 60);
        int totalSS = ((total % 3600) % 60);

        return format("%02d:%02d:%02d",totalHH,totalMM,totalSS);
//        String totalSumTime = totalHH + ":" + totalMM + ":" + totalSS;
//        return totalSumTime;
    }

    // String yyyy-MM-dd HH:mm:ss 형식으로 return; 현재 시간
    private static String nowDateTime() {
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        String nowYear = String.valueOf(nowSeoul.getYear());
        String nowMonth = String.valueOf(nowSeoul.getMonthValue());
        String nowDay = String.valueOf(nowSeoul.getDayOfMonth());
        String nowTime = nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return nowYear + "-" + nowMonth + "-" + nowDay + " " + nowTime;
    }

    // 전 날 오전 5시 기준 데이터 포맷
    private static Date dateTimeFormat(String setDate) throws ParseException {
        String setToday = setDate + "05:00:00"; //어제 날짜 + 오전5시 -> 조건을 걸기 위해 만들어줌?
        return sdtf.parse(setToday);
    }


    public static void main(String[] args) throws ParseException {

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString();
        Calendar today = todayCalendar(date); // 현재 시간 기준 날짜
        todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간
        String setToday = dateFormat(today);
        String setToday2  = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (date == setToday) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
        System.out.println("=======================");
        if (date == setToday2) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
        System.out.println("=======================");
        if (setToday == setToday2) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
        System.out.println(date);
        System.out.println(setToday);
        System.out.println(setToday2);
        System.out.println("=======================");

        String nowTime1 = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String nowTime2 = stf.format(today.getTime());
        String nowTime3 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        if (nowTime1 == nowTime2){
            System.out.println("1=2");
        } else {
            System.out.println("1!=2");
        }
        System.out.println("=======================");
        if (nowTime1 == nowTime3){
            System.out.println("1=3");
        } else {
            System.out.println("1!=3");
        }
        System.out.println("=======================");
        if (nowTime2 == nowTime3) {
            System.out.println("2=3");
        } else {
            System.out.println("2!=3");
        }
        System.out.println(nowTime1);
        System.out.println(nowTime2);
        System.out.println(nowTime3);
    }
}
