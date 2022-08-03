package com.team.unanimous.stompHandler;

import com.team.unanimous.exceptionHandler.CustomException;
import com.team.unanimous.exceptionHandler.ErrorCode;
import com.team.unanimous.model.chat.ChatRoom;
import com.team.unanimous.model.chat.ChatRoomUser;
import com.team.unanimous.model.meeting.MeetingUser;
import com.team.unanimous.model.user.User;
import com.team.unanimous.repository.chat.ChatRoomRepository;
import com.team.unanimous.repository.chat.ChatRoomUserRepository;
import com.team.unanimous.repository.meeting.MeetingUserRepository;
import com.team.unanimous.repository.user.UserRepository;
import com.team.unanimous.security.jwt.JwtDecoder;
import com.team.unanimous.service.ChatMessageService;
import com.team.unanimous.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    private final ChatRoomRepository chatRoomRepository;

    //Controller에 가기전에 이곳을 먼저 들리게 된다. 그것이 인터셉터의 역할
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // HTTP의 Request Response처럼
        // WebSocket은 message와 channel을 갖게된다.
        // accessor 을 이용하면 내용에 패킷에 접근할 수 있게된다.
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 접근했을때 COMMAND HEADER의 값을 확인 한다.
        // 만약 CONNET라면 -> 초기 연결임
        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
            // 토큰의 값만 확인 (로그인 여부를 확인하기 위함)
            // 헤더의 토큰값을 빼오기
            String jwtToken = accessor.getFirstNativeHeader("token").substring(7);
            if (jwtDecoder.decodeUsername(jwtToken)==null){
                throw new CustomException(ErrorCode.USER_NOT_FOUND);
            }
        }

        //만약 COMMAND가 SUBSCRIBE 즉 메세지를 주고 받기전 구독하는 것이라면
        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
            // roomId를 URL로 전송해주고 있어 추출 필요
            String roomId = chatMessageService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
//
//            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
//            // sessionId는 현재들어와있는 유저를 확인하기 위함이다.
            String sessionId = (String) message.getHeaders().get("simpSessionId");
//            //sessionId와 roomId를 맵핑
            chatRoomService.setUserEnterInfo(sessionId, roomId);

            // 구독했다는 것은 처음 입장했다는 것이므로 입장 메시지를 발송한다.
            // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
            String jwtToken = accessor.getFirstNativeHeader("token").substring(7);
            String username = jwtDecoder.decodeUsername(jwtToken);
            User user = userRepository.findUserByUsername(username);
            ChatRoom chatRoom = chatRoomRepository.findChatRoomById(Long.valueOf(roomId));

            List<ChatRoomUser> chatRoomUsers = chatRoomUserRepository.findAllByChatRoom(chatRoom);
            if (chatRoomUsers.size() > 7){
                throw new CustomException(ErrorCode.MEETING_MEMBER_HAS_FULL);
            }

            ChatRoomUser chatRoomUser = new ChatRoomUser(user,chatRoom);
            chatRoomUserRepository.save(chatRoomUser);

            log.info("SUBSCRIBED {}, {}", username, roomId);
        }

        //룸을 이동하게 된다면 -> DISCONNET 시킨다 ->
        //채팅방을 나가는경우
        else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료

            // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            //나갈떄 redis 맵에서 roomId와 sessionId의 매핑을 끊어줘야 하기때문에 roomId찾고
            String roomId = chatRoomService.getUserEnterRoomId(sessionId);

            String token = Optional.ofNullable(accessor.getFirstNativeHeader("token").substring(7)).orElse("UnknownUser");
            String username = jwtDecoder.decodeUsername(token);
            User user = userRepository.findUserByUsername(username);
            chatRoomUserRepository.deleteByUser_Id(user.getId());
            // 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
            chatRoomService.removeUserEnterInfo(sessionId);
            log.info("DISCONNECT {}, {}", sessionId, roomId);
        }
        return message;
    }
}
