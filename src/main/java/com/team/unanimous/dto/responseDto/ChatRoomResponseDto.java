package com.team.unanimous.dto.responseDto;

import com.team.unanimous.model.chat.ChatRoom;
import com.team.unanimous.model.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class ChatRoomResponseDto {

    private Long roomId;
    private String chatRoomName;

    private List<NicknameResponseDto> userList;

    private int userCnt;

    public ChatRoomResponseDto(ChatRoom chatRoom, int userCnt, List<NicknameResponseDto> userList){
        this.roomId = chatRoom.getId();;
        this.chatRoomName = chatRoom.getChatRoomName();
        this.userCnt = userCnt;
    }
}

