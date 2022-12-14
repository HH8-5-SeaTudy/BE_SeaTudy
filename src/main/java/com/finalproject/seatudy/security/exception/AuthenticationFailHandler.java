package com.finalproject.seatudy.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AuthenticationFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("토큰인증에 실패하였습니다.");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                new ObjectMapper().writeValueAsString(
                        ResponseDto.fail("AUTHORIZE_FAIL",  exception.getMessage())
                )
        );
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
