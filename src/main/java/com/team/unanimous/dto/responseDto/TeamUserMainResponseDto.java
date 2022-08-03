package com.team.unanimous.dto.responseDto;

import com.team.unanimous.model.TeamImage;
import com.team.unanimous.model.team.Team;
import lombok.Getter;

import java.util.List;

@Getter
public class TeamUserMainResponseDto {

    private Long teamid;

    private String teamname;

    private String teamImage;

    private String uuid;
    private List<NicknameResponseDto> user;

    public TeamUserMainResponseDto(Team team, List<NicknameResponseDto> nicknameResponseDtos) {
        this.teamid = team.getId();
        this.teamname = team.getTeamname();
        this.uuid = team.getUuid();
        this.teamImage = team.getTeamImage().getTeamImageUrl();
        this.user = nicknameResponseDtos;
    }
}
