package com.team.unanimous.dto.responseDto;

import com.team.unanimous.model.meeting.Issue;
import com.team.unanimous.model.meeting.Meeting;
import com.team.unanimous.model.user.User;
import lombok.Getter;
import java.util.List;

@Getter
public class MeetingResponseDto {

    private Long meetingId;

    private Meeting.Status meetingStatus;

    private String meetingTitle;

    private String meetingDate;

    private String meetingTime;

    private String meetingSum;

    private String meetingTheme;

    private String meetingCreator;

    private String meetingDuration;

    private String meetingOverTime;

    private List<Issue> issues;

    public MeetingResponseDto(Meeting meeting, User user){
        this.meetingId = meeting.getId();
        this.meetingStatus = meeting.getMeetingStatus();
        this.meetingTitle = meeting.getMeetingTitle();
        this.meetingDate = meeting.getMeetingDate();
        this.meetingTime = meeting.getMeetingTime();
        this.meetingSum = meeting.getMeetingSum();
        this.meetingTheme = meeting.getMeetingTheme();
        this.meetingDuration = meeting.getMeetingDuration();
        this.meetingOverTime = meeting.getMeetingOverTime();
        this.issues = meeting.getMeetingIssue();
        this.meetingOverTime = meeting.getMeetingOverTime();
        this.meetingCreator =  user.getNickname();
    }
}
