package com.team.unanimous.dto.requestDto;

import com.team.unanimous.model.Image;
import com.team.unanimous.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDto {
    private Long userId;
    private String username;
    private String nickname;
    private Image imageUrl;

    public static UserRequestDto fromUser(User user) {

        UserRequestDto requestDto = new UserRequestDto();

        requestDto.userId = user.getId();
        requestDto.username = user.getUsername();
        requestDto.nickname = user.getNickname();
        requestDto.imageUrl = user.getImage();

        return requestDto;
    }
}
