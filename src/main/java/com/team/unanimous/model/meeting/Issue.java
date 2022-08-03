package com.team.unanimous.model.meeting;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.team.unanimous.dto.requestDto.IssueRequestDto;
import com.team.unanimous.dto.requestDto.ResultRequestDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String issueContent;

    @Column
    private String issueResult;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    public void updateIssue(IssueRequestDto requestDto){
        this.issueContent = requestDto.getIssueContent();
    }

    public void writeResult(ResultRequestDto requestDto){
        this.issueResult = requestDto.getIssueResult();
    }
}
