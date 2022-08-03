package com.team.unanimous.model.team;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.team.unanimous.dto.requestDto.TeamRequestDto;
import com.team.unanimous.model.Image;
import com.team.unanimous.model.TeamImage;
import com.team.unanimous.model.meeting.Meeting;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String teamname;

    @Column
    private String uuid;

    @Column(nullable = false)
    private String teamManager;

    @OneToOne
    @JoinColumn(name = "teamImageId")
    private TeamImage teamImage;

    @JsonManagedReference
    @OneToMany(mappedBy = "team")
    private List<TeamUser> userList;

    @JsonManagedReference
    @OneToMany(mappedBy = "team")
    private List<Meeting> meetingList;

    public void updateTeam(TeamRequestDto requestDto){

        this.teamname = requestDto.getTeamname();
    }

    public void updateImage(TeamImage teamImage){
        this.teamImage = teamImage;
    }
}
