package com.team.unanimous.model.meeting;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.team.unanimous.dto.requestDto.MeetingRequestDto;
import com.team.unanimous.model.Timestamped;
import com.team.unanimous.model.team.Team;
import com.team.unanimous.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Meeting extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum Status{
        NOW, DONE, YET
    }

    @Column(nullable = false)
    private Status meetingStatus;

    @Column(nullable = false)
    private String meetingTitle;

    @Column(nullable = false)
    private String meetingDate;

    @Column(nullable = false)
    private String meetingTime;

    @Column(nullable = false)
    private String meetingSum;

    @Column(nullable = false)
    private String meetingTheme;

    @Column
    private String meetingDuration;

    @Column
    private String meetingOverTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User meetingCreator;

    @JsonManagedReference
    @OneToMany(mappedBy = "meeting",fetch = FetchType.LAZY)
    private List<Issue> meetingIssue;

    @JsonManagedReference
    @OneToMany(mappedBy = "meeting",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MeetingUser> user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public void updateMeeting(MeetingRequestDto requestDto){
        this.meetingTitle = requestDto.getMeetingTitle();
        this.meetingDate = requestDto.getMeetingDate();
        this.meetingTime = requestDto.getMeetingTime();
        this.meetingSum = requestDto.getMeetingSum();
        this.meetingTheme = requestDto.getMeetingTheme();
        this.meetingDuration = requestDto.getMeetingDuration();
    }
}
