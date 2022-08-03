package com.team.unanimous.controller;

import com.team.unanimous.dto.requestDto.MeetingRequestDto;
import com.team.unanimous.dto.responseDto.MeetingResponseDto;
import com.team.unanimous.security.UserDetailsImpl;
import com.team.unanimous.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    // 미팅 예약하기 생성
    @PostMapping("/api/teams/{teamId}/meetings")
    public ResponseEntity createMeeting(@RequestBody MeetingRequestDto meetingRequestDto,
                                        @PathVariable Long teamId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return meetingService.createMeeting(meetingRequestDto,userDetails,teamId);
    }

    // 미팅 바로 시작하기
    @PostMapping("/api/teams/{teamId}/meetings/now")
    public ResponseEntity createMeetingNow(@RequestBody MeetingRequestDto meetingRequestDto,
                                           @PathVariable Long teamId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        return meetingService.createMeetingNow(meetingRequestDto, userDetails, teamId);
    }

    // 미팅 목록
    @GetMapping("/api/teams/{teamId}/meetings")
    public List<MeetingResponseDto> getMeetings(@PathVariable Long teamId){
        return meetingService.getMeetings(teamId);
    }

    // 예약중인 미팅 목록
    @GetMapping("/api/teams/{teamId}/meetings/yet")
    public List<MeetingResponseDto> getYetMeetings(@PathVariable Long teamId){
        return meetingService.getYetMeetings(teamId);
    }

    // 진행중인 미팅 목록
    @GetMapping("/api/teams/{teamId}/meetings/now")
    public List<MeetingResponseDto> getNowMeetings(@PathVariable Long teamId){
        return meetingService.getNowMeetings(teamId);
    }

    // 이전 미팅 목록
    @GetMapping("/api/teams/{teamId}/meetings/done")
    public List<MeetingResponseDto> getDoneMeetings(@PathVariable Long teamId){
        return meetingService.getDoneMeetings(teamId);
    }

    // 특정 미팅 조회
    @GetMapping("/api/meetings/{meetingId}")
    public MeetingResponseDto getMeeting(@PathVariable Long meetingId){
        return meetingService.getMeeting(meetingId);
    }

    // 미팅 프로필 수정
    @PatchMapping("/api/meetings/{meetingId}")
    public ResponseEntity updateMeeting(@RequestBody MeetingRequestDto requestDto,
                                        @PathVariable Long meetingId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return meetingService.updateMeeting(requestDto,meetingId,userDetails);
    }

    // 미팅 삭제
    @DeleteMapping("/api/meetings/{meetingId}")
    public ResponseEntity deleteMeeting(@PathVariable Long meetingId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return meetingService.deleteMeeting(meetingId,userDetails);
    }
}
