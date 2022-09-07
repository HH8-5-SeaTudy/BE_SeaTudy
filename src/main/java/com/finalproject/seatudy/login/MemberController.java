package com.finalproject.seatudy.login;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.finalproject.seatudy.dto.request.LoginRequestDto;
import com.finalproject.seatudy.dto.request.MemberRequestDto;
import com.finalproject.seatudy.dto.response.ResponseDto;
import com.finalproject.seatudy.login.google.GoogleMemberService;
import com.finalproject.seatudy.login.kakao.KaKaoMemberService;
import com.finalproject.seatudy.login.naver.NaverMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {

  private final MemberService memberService;
  private final KaKaoMemberService kaKaoMemberService;
  private final NaverMemberService naverMemberService;
  private final GoogleMemberService googleMemberService;



  @RequestMapping(value = "/api/member/signup", method = RequestMethod.POST)
  public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto memberRequestDto) {
    return memberService.createMember(memberRequestDto);
  }

  @RequestMapping(value = "/api/member/login", method = RequestMethod.POST)
  public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto,
      HttpServletResponse response) {
    return memberService.login(loginRequestDto, response);
  }

  @RequestMapping(value = "/api/auth/member/logout", method = RequestMethod.POST)
  public ResponseDto<?> logout(HttpServletRequest request) {
    return memberService.logout(request);
  }

  //카카오 로그인
  @GetMapping("/api/v1/members/kakaoLogin")
  @ResponseBody
  public ResponseDto<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
    return kaKaoMemberService.kakaoLogin(code, response);
  }

  //네이버 로그인
  @GetMapping("/api/v1/members/naverLogin")
  public ResponseDto<?> naverLogin(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws JsonProcessingException {
    return naverMemberService.naverLogin(code, state, response);
  }

  //구글 로그인
  @GetMapping("/api/v1/members/googleLogin")
  public ResponseDto<?> googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
    return googleMemberService.googleLogin(code, response);
  }
}
