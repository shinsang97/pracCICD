package com.team.unanimous.controller;

import com.team.unanimous.dto.requestDto.IssueRequestDto;
import com.team.unanimous.dto.requestDto.ResultRequestDto;
import com.team.unanimous.dto.responseDto.IssueResponseDto;
import com.team.unanimous.security.UserDetailsImpl;
import com.team.unanimous.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    // 미팅 예약하기 안건 등록
    @PostMapping("/api/meetings/{meetingId}/issues")
    public ResponseEntity createIssueYet(@PathVariable Long meetingId,
                                      @RequestBody IssueRequestDto requestDto){
        return issueService.createIssueYet(meetingId,requestDto);
    }

    // 미팅 바로 시작하기 안건 등록
    @PostMapping("/api/meetings/{meetingId}/issues/now")
    public ResponseEntity createIssueNow(@PathVariable Long meetingId,
                                         @RequestBody IssueRequestDto requestDto){
        return issueService.createIssueNow(meetingId,requestDto);
    }

    // 미팅 예약하기 안건 수정
    @PatchMapping("/api/meetings/{meetingId}/issues/{issueId}")
    public ResponseEntity updateIssue(@RequestBody IssueRequestDto requestDto,
                                      @PathVariable Long meetingId,
                                      @PathVariable Long issueId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return issueService.updateIssue(requestDto, meetingId, issueId, userDetails);
    }
    // 미팅 바로 시작하기 안건 수정
    @PatchMapping("/api/meetings/{meetingId}/issues/{issueId}/now")
    public ResponseEntity updateIssueNow(@RequestBody IssueRequestDto requestDto,
                                      @PathVariable Long meetingId,
                                      @PathVariable Long issueId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return issueService.updateIssueNow(requestDto, meetingId, issueId, userDetails);
    }

    // 미팅 예약하기 안건 삭제
    @DeleteMapping("/api/meetings/{meetingId}/issues/{issueId}")
    public ResponseEntity deleteIssue(@PathVariable Long meetingId,
                                      @PathVariable Long issueId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return issueService.deleteIssue(meetingId, issueId, userDetails);
    }

    // 미팅 바로 시작하기 안건 삭제
    @DeleteMapping("/api/meetings/{meetingId}/issues/{issueId}/now")
    public ResponseEntity deleteIssueNow(@PathVariable Long meetingId,
                                      @PathVariable Long issueId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return issueService.deleteIssueNow(meetingId, issueId, userDetails);
    }

    // 안건 목록
    @GetMapping("/api/meetings/{meetingId}/issues")
    public List<IssueResponseDto> getIssues(@PathVariable Long meetingId){
        return issueService.getIssues(meetingId);
    }

    // 안건별 회의 결과 작성
    @PatchMapping("/api/meetings/{meetingId}/issues/{issueId}/result")
    public ResponseEntity writeResult(@PathVariable Long meetingId,
                                      @PathVariable Long issueId,
                                      @RequestBody ResultRequestDto requestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return issueService.writeResult(meetingId, issueId, requestDto, userDetails);
    }
}
