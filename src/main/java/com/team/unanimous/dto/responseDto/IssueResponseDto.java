package com.team.unanimous.dto.responseDto;

import com.team.unanimous.model.meeting.Issue;
import lombok.Getter;

@Getter
public class IssueResponseDto {

    private Long issueId;

    private String issueContent;

    private String issueResult;

    public IssueResponseDto(Issue issue) {
        this.issueId = issue.getId();
        this.issueContent = issue.getIssueContent();
        this.issueResult = issue.getIssueResult();
    }
}
