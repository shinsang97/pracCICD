package com.team.unanimous.repository.issue;

import com.team.unanimous.model.meeting.Issue;
import com.team.unanimous.model.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue,Long> {

    Issue findIssueById(Long issueId);

}
