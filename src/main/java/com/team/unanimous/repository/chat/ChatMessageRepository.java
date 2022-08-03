package com.team.unanimous.repository.chat;

import com.team.unanimous.model.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    ChatMessage findByRoomId(String roomId);
}
