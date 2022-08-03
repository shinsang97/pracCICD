package com.team.unanimous.dto.responseDto;

import lombok.Getter;

@Getter
public class TeamResponseDto {

    private Long id;
    private String teamname;

    private String uuid;
    private String teamImage;

    public TeamResponseDto(Long id, String teamname, String uuid, String teamImage) {
        this.id = id;
        this.teamname = teamname;
        this.uuid = uuid;
        this.teamImage=teamImage;
    }


}
