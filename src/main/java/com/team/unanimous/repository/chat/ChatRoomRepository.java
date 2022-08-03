package com.team.unanimous.repository.chat;

import com.team.unanimous.model.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    ChatRoom findChatRoomById(Long roomId);

}
