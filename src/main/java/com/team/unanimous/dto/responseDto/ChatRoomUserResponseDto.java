package com.team.unanimous.dto.responseDto;

import com.team.unanimous.model.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class ChatRoomUserResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private String profileUrl;

    public ChatRoomUserResponseDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
//        this.profileUrl = user.getProfileUrl();
    }
}
