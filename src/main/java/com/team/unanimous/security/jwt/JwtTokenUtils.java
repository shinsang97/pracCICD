package com.team.unanimous.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.team.unanimous.security.UserDetailsImpl;

import java.util.Date;

public final class JwtTokenUtils {

    private static final int SEC = 1;
    private static final int MINUTE = 60 * SEC;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    // JWT 토큰의 유효기간: 2시간 (단위: seconds)
    private static final int JWT_TOKEN_VALID_SEC = 2 * HOUR;
    // JWT 토큰의 유효기간: 2시간 (단위: milliseconds)
    private static final int JWT_TOKEN_VALID_MILLI_SEC = JWT_TOKEN_VALID_SEC * 1000;
    // Refresh Token의 유효기간: 3일 (단위: seconds)
    private static final int REFRESH_TOKEN_VALID_SEC = 3 * DAY;
    // Refresh Token의 유효기간: 3일 (단위: milliseconds)
    private static final int REFRESH_TOKEN_VALID_MILLI_SEC = REFRESH_TOKEN_VALID_SEC * 1000;

    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_EXPIRED_DATE = "EXPIRED_DATE";
    public static final String CLAIM_USER_NAME = "USER_NAME";
    public static final String CLAIM_USER_NICKNAME = "USER_NICKNAME";
    public static final String CLAIM_USER_IMAGE = "USER_IMAGE";
    public static final String JWT_SECRET = "jwt_secret_!@#$%";

    public static String generateJwtToken(UserDetailsImpl userDetails) {
        String token = null;
        try {
            token = JWT.create()
                    .withIssuer("Unanimous")
                    .withClaim(CLAIM_USER_ID, userDetails.getUser().getId())
                    .withClaim(CLAIM_USER_NAME, userDetails.getUsername())
                    .withClaim(CLAIM_USER_NICKNAME, userDetails.getUser().getNickname())
                    .withClaim(CLAIM_USER_IMAGE, userDetails.getUser().getImage().getImageUrl())
                    // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
                    .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + JWT_TOKEN_VALID_MILLI_SEC))
                    .sign(generateAlgorithm());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return token;
    }

    //리프레시 토큰 생성
    public static String generaterefreshToken(UserDetailsImpl userDetails){
        String refreshToken = null;
        try {
            refreshToken = JWT.create()
                    .withIssuer("Unanimous")
                    .withClaim(CLAIM_USER_ID, userDetails.getUser().getId())
                    .withClaim(CLAIM_USER_NAME, userDetails.getUsername())
                    .withClaim(CLAIM_USER_NICKNAME, userDetails.getUser().getNickname())
                    .withClaim(CLAIM_USER_IMAGE, userDetails.getUser().getImage().getImageUrl())
                    // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
                    .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_MILLI_SEC))
                    .sign(generateAlgorithm());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return refreshToken;
    }

    public static String generateLogoutToken() {
        String token = null;

        try {
            token = JWT.create()
                    .withIssuer("Shinsang")
                    .withClaim(CLAIM_USER_NAME, "dummyName")
                    // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
                    .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + 100))
                    .sign(generateAlgorithm());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return token;
    }

    private static Algorithm generateAlgorithm() {
        return Algorithm.HMAC256(JWT_SECRET);
    }
}