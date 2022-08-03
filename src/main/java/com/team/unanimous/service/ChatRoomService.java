package com.team.unanimous.service;

import com.team.unanimous.dto.requestDto.ChatRoomRequestDto;
import com.team.unanimous.dto.responseDto.ChatRoomResponseDto;
import com.team.unanimous.dto.responseDto.NicknameResponseDto;
import com.team.unanimous.exceptionHandler.CustomException;
import com.team.unanimous.exceptionHandler.ErrorCode;
import com.team.unanimous.model.chat.ChatRoom;
import com.team.unanimous.model.chat.ChatRoomUser;
import com.team.unanimous.model.meeting.Meeting;
import com.team.unanimous.model.meeting.MeetingUser;
import com.team.unanimous.model.user.User;
import com.team.unanimous.repository.chat.ChatRoomRepository;
import com.team.unanimous.repository.chat.ChatRoomUserRepository;
import com.team.unanimous.repository.meeting.MeetingRepository;
import com.team.unanimous.repository.user.UserRepository;
import com.team.unanimous.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    //레디스 저장소 사용
    //key hashKey value 구조
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final MeetingRepository meetingRepository;
    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId 와 채팅룸 id 를 맵핑한 정보 저장

    // 채팅방 생성
    public Long createChatRoom(Long meetingId,
                                   UserDetailsImpl userDetails) {
        if (!(userRepository.findByUsername(userDetails.getUsername()).isPresent())){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        Meeting meeting = meetingRepository.findMeetingById(meetingId);

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName(meeting.getMeetingTitle()+"의 회의룸")
                .build();
        chatRoomRepository.save(chatRoom);

        return chatRoom.getId();
    }

    // 개별 채팅방 조회
    public ChatRoomResponseDto getEachChatRoom(Long roomId, UserDetailsImpl userDetails) {
        if (!(userRepository.findByUsername(userDetails.getUsername()).isPresent())){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        List<ChatRoomUser> chatRoomUsers = chatRoomUserRepository.findAllByChatRoom(chatRoom);
        int userCnt = chatRoomUsers.size();
        List<NicknameResponseDto> nicknameResponseDtos = new ArrayList<>();
        for (ChatRoomUser chatRoomUser : chatRoomUsers){
            User user = chatRoomUser.getUser();
            NicknameResponseDto nicknameResponseDto = new NicknameResponseDto(user);
            nicknameResponseDtos.add(nicknameResponseDto);
        }

        return new ChatRoomResponseDto(chatRoom,userCnt,nicknameResponseDtos);
    }

    // 유저가 입장한 채팅방 ID 와 유저 세션 ID 맵핑 정보 저장
    //Enter라는 곳에 sessionId와 roomId를 맵핑시켜놓음
    public void setUserEnterInfo(String sessionId, String roomId) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }

    // 유저 세션으로 입장해 있는 채팅방 ID 조회
    public String getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저 세션정보와 맵핑된 채팅방 ID 삭제
    //한 유저는 하나의 룸 아이디에만 맵핑되어있다!
    // 실시간으로 보는 방은 하나이기 떄문이다.
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }
}
