package com.team.unanimous.controller;

import com.team.unanimous.dto.requestDto.ChatMessageRequestDto;
import com.team.unanimous.model.chat.ChatMessage;
import com.team.unanimous.model.user.User;
import com.team.unanimous.repository.user.UserRepository;
import com.team.unanimous.security.jwt.JwtDecoder;
import com.team.unanimous.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    //왜 안되나요???

    private final ChatMessageService chatMessageService;
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;


    // 채팅 메시지를 @MessageMapping 형태로 받는다
    // 웹소켓으로 publish 된 메시지를 받는 곳이다
    @MessageMapping("/api/chat/message")
    public void message(@RequestBody ChatMessageRequestDto messageRequestDto, @Header("token") String token) {

        // 로그인 회원 정보를 들어온 메시지에 값 세팅
        String nickname = jwtDecoder.decodeNickname(token.substring(7));
        System.out.println(nickname);
        Optional<User> user1 = userRepository.findByNickname(nickname);
        User user = user1.get();
        messageRequestDto.setNickname(user.getNickname());
        messageRequestDto.setSender(user.getUsername());

        // 메시지 생성 시간 삽입
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String dateResult = sdf.format(date);
        messageRequestDto.setCreatedAt(dateResult);

        User user2 = userRepository.findUserByNickname(nickname);

        // DTO 로 채팅 메시지 객체 생성
        ChatMessage chatMessage = new ChatMessage(messageRequestDto, user2);

        // 웹소켓 통신으로 채팅방 토픽 구독자들에게 메시지 보내기
        chatMessageService.sendChatMessage(chatMessage);

        // MySql DB에 채팅 메시지 저장
        chatMessageService.save(chatMessage);
    }
}
