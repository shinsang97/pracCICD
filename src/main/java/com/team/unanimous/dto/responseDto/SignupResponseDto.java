package com.team.unanimous.dto.responseDto;

import com.team.unanimous.model.user.User;
import lombok.Getter;

@Getter
public class SignupResponseDto {
    private Long userId;
    private String message;

    public SignupResponseDto(User user,String message) {
        this.userId = user.getId();
        this.message = message;
    }
}
