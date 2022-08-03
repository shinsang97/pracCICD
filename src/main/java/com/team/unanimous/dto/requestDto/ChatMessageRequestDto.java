package com.team.unanimous.dto.requestDto;

import com.team.unanimous.model.chat.ChatMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequestDto {

    private ChatMessage.MessageType type;
    private String roomId;
    private String nickname;
    private String sender;
    private String message;
    private String createdAt;
}
