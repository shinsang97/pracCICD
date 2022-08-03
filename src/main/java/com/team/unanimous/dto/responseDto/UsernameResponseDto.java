package com.team.unanimous.dto.responseDto;

import lombok.Getter;

@Getter
public class UsernameResponseDto {

    private String username;

    private String message;
    public UsernameResponseDto(String username, String message) {
        this.username = username;
        this.message = message;
    }
}
