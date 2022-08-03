//package com.team.unanimous.Google;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.team.unanimous.model.user.User;
//import com.team.unanimous.repository.user.UserRepository;
//import com.team.unanimous.security.UserDetailsImpl;
//import com.team.unanimous.security.jwt.JwtTokenUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.transaction.Transactional;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class KakaoUserService {
//
//    private final UserRepository userRepository;
//
//    private final PasswordEncoder passwordEncoder;
//
//    @Transactional
//    public void kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
//        // 1. "인가 코드"로 "액세스 토큰" 요청
//        String accessToken = getAccessToken(code);
//        // 2. 토큰으로 카카오 API 호출
//        SocialLoginInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
//        // 3. 필요시에 회원가입
//        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);
//        // 4. 강제 로그인 처리
//        forceLogin(kakaoUser,response);
//    }
//    private String getAccessToken(String code) throws JsonProcessingException {
//        // HTTP Header 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//// HTTP Body 생성
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", "90f76c46b9cfff32515a72c551d29c33"); //본인의 REST API키입력
//        body.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
//        body.add("code", code);
//
//// HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
//                new HttpEntity<>(body, headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://kauth.kakao.com/oauth/token",
//                HttpMethod.POST,
//                kakaoTokenRequest,
//                String.class
//        );
//
//// HTTP 응답 (JSON) -> 액세스 토큰 파싱
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        return jsonNode.get("access_token").asText();
//    }
//    private SocialLoginInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
//        // HTTP Header 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//// HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                kakaoUserInfoRequest,
//                String.class
//        );
//
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        Long id = jsonNode.get("id").asLong();
//        String nickname = jsonNode.get("properties")
//                .get("nickname").asText();
//        String email = jsonNode.get("kakao_account")
//                .get("email").asText();
//        String profileImgUrl = jsonNode.get("properties")
//                .get("profile_image").asText();
//
//        System.out.println("카카오 사용자 정보: " + id + ", " + nickname + ", " + email + ", " + profileImgUrl);
//        return new SocialLoginInfoDto(id, nickname, email, profileImgUrl);
//    }
//    private User registerKakaoUserIfNeeded(SocialLoginInfoDto kakaoUserInfo) {
//        // DB 에 중복된 Kakao Id 가 있는지 확인
//        Long kakaoId = kakaoUserInfo.getId();
//        User kakaoUser = userRepository.findByKakaoId(kakaoId)
//                .orElse(null);
//        if (kakaoUser == null) {
//            // 회원가입
//            // username: kakao nickname
//            String nickname = kakaoUserInfo.getNickname();
//            // userImage: kakao profile image
//            String profileImgUrl = kakaoUserInfo.getProfileImgUrl();
//            // password: random UUID
//            String uuid = UUID.randomUUID().toString();
//            String password = passwordEncoder.encode(uuid);
//            // email: kakao email
//            String username = kakaoUserInfo.getEmail();
//
//            kakaoUser = new User(username, nickname, password, profileImgUrl ,kakaoId);
//            userRepository.save(kakaoUser);
//        }
//        return kakaoUser;
//    }
//    private void forceLogin(User kakaoUser, HttpServletResponse response) {
//        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // 토큰을 프론트에 넘김
//        UserDetailsImpl userDetails1 = ((UserDetailsImpl) authentication.getPrincipal());
//
//        System.out.println("userDetails1 : " + userDetails1.toString());
//
//        final String token = JwtTokenUtils.generateJwtToken(userDetails1);
//
//        System.out.println("token값:" + token);
//        response.addHeader("Authorization", "Bearer "+ " " + token);
//    }
//
//}

