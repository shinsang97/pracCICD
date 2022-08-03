package com.team.unanimous.security;

import com.team.unanimous.security.jwt.JwtTokenUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public static final String AUTH_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "RefreshToken";
    public static final String TOKEN_TYPE = "BEARER";

    public final RedisTemplate redisTemplate = null;


    //성공시 응답에 토큰을 추가하는 핸들러
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication) {
        final UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());
        // Token 생성
        final String token = JwtTokenUtils.generateJwtToken(userDetails);
        final String refreshToken = JwtTokenUtils.generaterefreshToken(userDetails);

        //실제 발급
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);
        response.addHeader(REFRESH_HEADER, TOKEN_TYPE + " " + refreshToken);
        System.out.println("token : " + token);
        System.out.println("refresh : " + refreshToken);
        }
//        //User nicakname 내려주기 - 동관 천재님꺼 참고
//        response.setContentType("application/json");
//        User user = userDetails.getUser();
//        LoginResponseDto loginResponseDto = new LoginResponseDto(user.getNickname(), true, token);
//        String result = mapper.writeValueAsString(loginResponseDto);
//        response.getWriter().write(result);
    }

