package com.team.unanimous.service;

import com.team.unanimous.model.chat.ChatMessage;
import com.team.unanimous.model.user.User;
import com.team.unanimous.repository.chat.ChatMessageRepository;
import com.team.unanimous.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    // destination 정보에서 roomId 추출
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    // 채팅방에 메시지 발송
    public void sendChatMessage(ChatMessage chatMessage) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);

        // 회의 입장
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 회의에 입장했습니다.");
            chatMessage.setSender("\uD83D\uDD14 [알림]");
        }
        // 회의 퇴장
        if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 회의에서 나갔습니다.");
            chatMessage.setSender("\uD83D\uDD14 [알림]");
        }
    }

    public void save(ChatMessage chatMessage) {
        ChatMessage message = new ChatMessage();
        message.setType(chatMessage.getType());
        message.setRoomId(chatMessage.getRoomId());
        message.setNickname(chatMessage.getNickname());
        message.setSender(chatMessage.getSender());
        message.setMessage(chatMessage.getMessage());
        chatMessageRepository.save(message);
    }

    public ChatMessage getChatMessageByRoomId(String roomId) {
//        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1);
//        pageable = PageRequest.of(page, 150);
        return chatMessageRepository.findByRoomId(roomId);
    }
}
