package com.team.unanimous.dto.requestDto;

import lombok.Getter;

@Getter
public class FindPasswordRequestDto {
    private String username;
    private String password;
    private String passwordCheck;
}
