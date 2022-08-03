package com.team.unanimous.controller;

import com.team.unanimous.dto.responseDto.ChatRoomResponseDto;
import com.team.unanimous.model.chat.ChatMessage;
import com.team.unanimous.security.UserDetailsImpl;
import com.team.unanimous.service.ChatMessageService;
import com.team.unanimous.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;


    // 채팅방 생성
    @PostMapping("/meetings/{meetingId}/rooms")
    public HashMap<String, Object> ChatRoomResponseDto(@PathVariable Long meetingId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        Long roomId = chatRoomService.createChatRoom(meetingId,userDetails);
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomId", roomId);
        return result;
    }

    // 채팅방 상세 조회
    @GetMapping("/rooms/{roomId}")
    public ChatRoomResponseDto getEachChatRoom(@PathVariable Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getEachChatRoom(roomId,userDetails);
    }

    // 채팅방 내 메시지 전체 조회
    @GetMapping("/rooms/{roomId}/messages")
    public ChatMessage getEachChatRoomMessages(@PathVariable String roomId) {
        return chatMessageService.getChatMessageByRoomId(roomId);
    }
}
