package com.team.unanimous.service;

import com.team.unanimous.dto.requestDto.IssueRequestDto;
import com.team.unanimous.dto.requestDto.ResultRequestDto;
import com.team.unanimous.dto.responseDto.IssueResponseDto;
import com.team.unanimous.exceptionHandler.CustomException;
import com.team.unanimous.exceptionHandler.ErrorCode;
import com.team.unanimous.model.meeting.Issue;
import com.team.unanimous.model.meeting.Meeting;
import com.team.unanimous.model.user.User;
import com.team.unanimous.repository.issue.IssueRepository;
import com.team.unanimous.repository.meeting.MeetingRepository;
import com.team.unanimous.repository.team.TeamRepository;
import com.team.unanimous.repository.user.UserRepository;
import com.team.unanimous.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    private final MeetingRepository meetingRepository;

    private final UserRepository userRepository;

    // 미팅 예약하기 안건 등록
    @Transactional
    public ResponseEntity createIssueYet(Long meetingId,
                                         IssueRequestDto requestDto){
        String issueContent = requestDto.getIssueContent().trim();
        if (issueContent == null){
            throw new CustomException(ErrorCode.EMPTY_ISSUE);
        } else if (issueContent.isEmpty()){
            throw new CustomException(ErrorCode.EMPTY_ISSUE);
        } else if (issueContent.equals("")){
            throw new CustomException(ErrorCode.EMPTY_ISSUE);
        } else if (issueContent.length() > 40){
            throw new CustomException(ErrorCode.ISSUE_LENGTH);
        }
        Meeting meeting = meetingRepository.findMeetingById(meetingId);
        if (meeting == null){
            throw new CustomException(ErrorCode.MEETING_NOT_FOUND);
        }else if (!(meeting.getMeetingStatus() == Meeting.Status.YET)){
            throw new CustomException(ErrorCode.MEETING_HAS_DONE);
        }

        Issue issue = Issue.builder()
                .issueContent(issueContent)
                .meeting(meeting)
                .build();
        issueRepository.save(issue);
        return ResponseEntity.ok("안건 등록 완료");
    }


    // 미팅 바로 시작하기 안건 등록
    @Transactional
    public ResponseEntity createIssueNow(Long meetingId,
                                         IssueRequestDto requestDto){
        String issueContent = requestDto.getIssueContent().trim();
        if (issueContent == null){
            throw new CustomException(ErrorCode.EMPTY_ISSUE);
        } else if (issueContent.isEmpty()){
            throw new CustomException(ErrorCode.EMPTY_ISSUE);
        } else if (issueContent.equals("")){
            throw new CustomException(ErrorCode.EMPTY_ISSUE);
        } else if (issueContent.length() > 40){
            throw new CustomException(ErrorCode.ISSUE_LENGTH);
        }
        Meeting meeting = meetingRepository.findMeetingById(meetingId);
        if (meeting == null){
            throw new CustomException(ErrorCode.MEETING_NOT_FOUND);
        }else if (!(meeting.getMeetingStatus() == Meeting.Status.NOW)){
            throw new CustomException(ErrorCode.MEETING_HAS_DONE);
        }

        Issue issue = Issue.builder()
                .issueContent(issueContent)
                .meeting(meeting)
                .build();
        issueRepository.save(issue);
        return ResponseEntity.ok("안건 등록 완료");
    }

    // 미팅 예약하기 안건 수정
    public ResponseEntity updateIssue(IssueRequestDto requestDto,
                                      Long meetingId,
                                      Long issueId,
                                      UserDetailsImpl userDetails){
        Meeting meeting = meetingRepository.findMeetingById(meetingId);
        if (meeting == null){
            throw new CustomException(ErrorCode.MEETING_NOT_FOUND);
        } else if (!(meeting.getMeetingStatus().equals(Meeting.Status.YET))){
            throw new CustomException(ErrorCode.MEETING_HAS_DONE);
        } else if (!(meeting.getMeetingCreator().getNickname().equals(userDetails.getUser().getNickname()))){
            throw new CustomException(ErrorCode.INVALID_AUTHORITY);
        }
        Issue issue = issueRepository.findIssueById(issueId);
        if (issue == null){
            throw new CustomException(ErrorCode.ISSUE_NOT_FOUND);
        }

        String issueContent = requestDto.getIssueContent().trim();
        if (issueContent == null){
            throw new CustomException(ErrorCode.EMPTY_ISSUE);
        } else if (issueContent.isEmpty()){
            throw new CustomException(ErrorCode.EMPTY_ISSUE);
        } else if (issueContent.equals("")){
            throw new CustomException(ErrorCode.EMPTY_ISSUE);
        } else if (issueContent.length() > 40){
            throw new CustomException(ErrorCode.ISSUE_LENGTH);
        }

        issue.updateIssue(requestDto);
        issueRepository.save(issue);
        return ResponseEntity.ok("안건 수정 완료");
    }

    // 미팅 바로 시작하기 안건 수정
    public ResponseEntity updateIssueNow(IssueRequestDto requestDto,
                                      Long meetingId,
                                      Long issueId,
                                      UserDetailsImpl userDetails){
        Meeting meeting = meetingRepository.findMeetingById(meetingId);
        if (meeting == null){
            throw new CustomException(ErrorCode.MEETING_NOT_FOUND);
        } else if (!(meeting.getMeetingStatus().equals(Meeting.Status.NOW))){
            throw new CustomException(ErrorCode.MEETING_HAS_DONE);
        } else if (!(meeting.getMeetingCreator().getNickname().equals(userDetails.getUser().getNickname()))){
            throw new CustomException(ErrorCode.INVALID_AUTHORITY);
        }
        Issue issue = issueRepository.findIssueById(issueId);
        if (issue == null){
            throw new CustomException(ErrorCode.ISSUE_NOT_FOUND);
        }

        String issueContent = requestDto.getIssueContent().trim();
        if (issueContent == null){
            throw new CustomException(ErrorCode.EMPTY_ISSUE);
        } else if (issueContent.isEmpty()){
            throw new CustomException(ErrorCode.EMPTY_ISSUE);
        } else if (issueContent.equals("")){
            throw new CustomException(ErrorCode.EMPTY_ISSUE);
        } else if (issueContent.length() > 40){
            throw new CustomException(ErrorCode.ISSUE_LENGTH);
        }

        issue.updateIssue(requestDto);
        issueRepository.save(issue);
        return ResponseEntity.ok("안건 수정 완료");
    }

    // 미팅 예약하기 안건 삭제
    public ResponseEntity deleteIssue(Long meetingId,
                                      Long issueId,
                                      UserDetailsImpl userDetails){
        Meeting meeting = meetingRepository.findMeetingById(meetingId);
        Issue issue = issueRepository.findIssueById(issueId);
        if (issue == null){
            throw new CustomException(ErrorCode.ISSUE_NOT_FOUND);
        } else if (!(meeting.getMeetingStatus().equals(Meeting.Status.YET))){
            throw new CustomException(ErrorCode.MEETING_HAS_DONE);
        } else if (!(meeting.getMeetingCreator().getNickname().equals(userDetails.getUser().getNickname()))){
            throw new CustomException(ErrorCode.INVALID_AUTHORITY);
        }

        issueRepository.delete(issue);
        return ResponseEntity.ok("안건 삭제 완료");
    }

    // 미팅 바로 시작하기 안건 삭제
    public ResponseEntity deleteIssueNow(Long meetingId,
                                      Long issueId,
                                      UserDetailsImpl userDetails){
        Meeting meeting = meetingRepository.findMeetingById(meetingId);
        Issue issue = issueRepository.findIssueById(issueId);
        if (issue == null){
            throw new CustomException(ErrorCode.ISSUE_NOT_FOUND);
        } else if (!(meeting.getMeetingStatus().equals(Meeting.Status.NOW))){
            throw new CustomException(ErrorCode.MEETING_HAS_DONE);
        } else if (!(meeting.getMeetingCreator().getNickname().equals(userDetails.getUser().getNickname()))){
            throw new CustomException(ErrorCode.INVALID_AUTHORITY);
        }

        issueRepository.delete(issue);
        return ResponseEntity.ok("안건 삭제 완료");
    }

    // 안건 목록
    public List<IssueResponseDto> getIssues(Long meetingId){
        Meeting meeting = meetingRepository.findMeetingById(meetingId);

        List<Issue> issues = meeting.getMeetingIssue();

        List<IssueResponseDto> issueResponseDtos = new ArrayList<>();
        for (Issue issue : issues){
            IssueResponseDto issueResponseDto = new IssueResponseDto(issue);
            issueResponseDtos.add(issueResponseDto);
        }
        return issueResponseDtos;
    }

    // 안건별 회의 결과 작성
    public ResponseEntity writeResult(Long meetingId,
                                      Long issueId,
                                      ResultRequestDto requestDto,
                                      UserDetailsImpl userDetails){
        Meeting meeting = meetingRepository.findMeetingById(meetingId);

        User user = userRepository.findUserById(userDetails.getUser().getId());
        if (!(meeting.getMeetingCreator().equals(user.getNickname()))){
            throw new CustomException(ErrorCode.INVALID_AUTHORITY);
        }

        Issue issue = issueRepository.findIssueById(issueId);

        issue.writeResult(requestDto);
        issueRepository.save(issue);
        return ResponseEntity.ok("결과 작성 완료");
    }
}
