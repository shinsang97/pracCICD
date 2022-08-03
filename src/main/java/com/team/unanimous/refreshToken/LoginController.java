package com.team.unanimous.refreshToken;


import com.team.unanimous.security.UserDetailsImpl;
import com.team.unanimous.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    // 로그인
    @PostMapping("/api/users/signup/{userId}")
    public ResponseEntity login(UserDetailsImpl userDetails, HttpServletResponse response) {

        // 어세스, 리프레시 토큰 발급 및 헤더 설정
        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail(), member.getRoles());
        jwtTokenProvider.setHeaderAccessToken(response, accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);
        // 리프레시 토큰 저장소에 저장
        tokenRepository.save(new RefreshToken(refreshToken));

        return ResponseEntity.ok().body("로그인 성공!");
    }
}
