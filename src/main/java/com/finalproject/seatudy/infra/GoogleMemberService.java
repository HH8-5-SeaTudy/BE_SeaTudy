package com.finalproject.seatudy.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.repository.MemberRepository;
import com.finalproject.seatudy.service.dto.response.GoogleUserDto;
import com.finalproject.seatudy.security.jwt.JwtTokenUtils;
import com.finalproject.seatudy.domain.LoginType;
import com.finalproject.seatudy.service.MemberService;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleMemberService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    @Value("${security.oauth2.google.client_id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${security.oauth2.google.client_secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${security.oauth2.google.redirect_uri}")
    private String GOOGLE_REDIRECT_URI;

    public ResponseDto<?> googleLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        String googleACTokens = getGoogleTokens(code);

        GoogleUserDto googleUserInfo = getGoogleUserInfo(googleACTokens);

        Member googleMember = registerGoogleUserIfNeed(googleUserInfo);

        String googleAC = jwtTokenUtils.generateJwtToken(googleMember);
        memberService.tokenToHeaders(googleAC, response);

        log.info("구글 로그인 완료: {}",googleMember.getEmail());
        return ResponseDto.success(
                GoogleUserDto.builder()
                        .id(googleMember.getMemberId())
                        .email(googleMember.getEmail())
                        .nickname(googleMember.getNickname())
                        .build());
    }

    private String getGoogleTokens(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code",code);
        body.add("client_id", GOOGLE_CLIENT_ID);
        body.add("client_secret", GOOGLE_CLIENT_SECRET);
        body.add("redirect_uri", GOOGLE_REDIRECT_URI);
        body.add("grant_type", "authorization_code");


        HttpEntity<MultiValueMap<String, String>> googleTokenRequest =
                new HttpEntity<>(body,headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                googleTokenRequest,
                String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }


    private GoogleUserDto getGoogleUserInfo(String googleACTokens) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleACTokens);
        headers.add("Content-type", "Content-Type: application/json;charset=UTF-8");


        HttpEntity<MultiValueMap<String, String>> googleUserInfoRequest =
                new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://www.googleapis.com/oauth2/v3/userinfo",
                HttpMethod.GET, googleUserInfoRequest, String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long googleId = jsonNode.get("sub").asLong();
        String email = jsonNode.get("email").asText();
        String name = jsonNode.get("name").asText();

        return GoogleUserDto.builder()
                .id(googleId)
                .email(email)
                .nickname(name)
                .build();
    }


    private Member registerGoogleUserIfNeed(GoogleUserDto googleUserInfo) {
        String email = googleUserInfo.getEmail();
        String name = googleUserInfo.getNickname();
        Member googleMember = memberRepository.findByEmail(email).orElse(null);

        if(googleMember == null) {
            String password = UUID.randomUUID().toString();

            googleMember = Member.builder()
                    .email(email)
                    .nickname(name)
                    .password(passwordEncoder.encode(password))
                    .loginType(LoginType.GOOGLE)
                    .build();

            memberRepository.save(googleMember);
        }
        return googleMember;
    }
}