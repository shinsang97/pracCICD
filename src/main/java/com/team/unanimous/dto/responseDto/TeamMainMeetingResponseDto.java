package com.team.unanimous.dto.responseDto;

import com.team.unanimous.model.user.User;
import lombok.Getter;

import java.util.List;

@Getter
public class TeamMainMeetingResponseDto {

    private String meetingTitle;
    private String meetingDate;

    private List<User> meetingUserList;

    public TeamMainMeetingResponseDto(String meetingTitle, String meetingDate, List<User> meetingUserList) {
        this.meetingTitle = meetingTitle;
        this.meetingDate = meetingDate;
        this.meetingUserList = meetingUserList;
    }
}
