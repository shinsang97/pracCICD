package com.team.unanimous.controller;


import com.team.unanimous.dto.requestDto.BanRequestDto;
import com.team.unanimous.dto.requestDto.NicknameRequestDto;
import com.team.unanimous.dto.requestDto.TeamInviteRequestDto;
import com.team.unanimous.dto.requestDto.TeamRequestDto;
import com.team.unanimous.dto.responseDto.TeamResponseDto;
import com.team.unanimous.dto.responseDto.TeamUserMainResponseDto;
import com.team.unanimous.dto.responseDto.TeamUserResponseDto;
import com.team.unanimous.exceptionHandler.CustomException;
import com.team.unanimous.exceptionHandler.ErrorCode;
import com.team.unanimous.security.UserDetailsImpl;
import com.team.unanimous.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;


    // Team Unanimous 참여하기
    @PostMapping("/api/teams/unanimous")
    public ResponseEntity joinUnanimous(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return teamService.joinUnanimous(userDetails);
    }
    // 팀 선택 페이지
    @GetMapping("/api/teams")
    public List<TeamUserResponseDto> getTeams(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return teamService.getTeams(userDetails);
    }

    // 팀 생성
    @PostMapping("/api/teams")
    public ResponseEntity createTeam(@RequestBody TeamRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if (userDetails == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return teamService.createTeam(requestDto, userDetails);
    }

    //초대받은 팀 찾기
    @PostMapping("/api/teams/auth-code")
    public TeamResponseDto findTeamUuid(@RequestBody TeamInviteRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if (userDetails == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return teamService.findTeamUuid(requestDto);
    }


    // 팀 가입하기
    @PostMapping("/api/teams/join")
    public ResponseEntity JoinTeam(@RequestBody TeamInviteRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if (userDetails == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return teamService.JoinTeam(requestDto, userDetails);
    }

    // 팀 메인 게시판
    @GetMapping("/api/teams/{teamId}")
    public TeamUserMainResponseDto getMain(@PathVariable Long teamId){
        return teamService.getMain(teamId);
    }

    // 팀 프로필 수정
    @PatchMapping("/api/teams/{teamId}")
    public ResponseEntity updateTeam(@RequestParam("teamImage") MultipartFile multipartFile,
                                     @PathVariable Long teamId,
                                     @RequestPart("teamName") TeamRequestDto requestDto,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails)throws IOException {
        return teamService.updateTeam(multipartFile,teamId,requestDto,userDetails);
    }

    // 팀원 강퇴
    @DeleteMapping("/api/teams/{teamId}/ban")
    public ResponseEntity banUser(@PathVariable Long teamId,
                                  @RequestBody BanRequestDto requestDto,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        return teamService.banUser(teamId,requestDto,userDetails);
    }

    // 팀 탈퇴
    @DeleteMapping("/api/teams/{teamId}/exit")
    public ResponseEntity exitTeam(@PathVariable Long teamId,
                                  @RequestBody BanRequestDto requestDto,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        return teamService.exitTeam(teamId,requestDto,userDetails);
    }

    // 팀장 위임
    @PostMapping("/api/teams/{teamId}/manager")
    public ResponseEntity changeTeamManager(@RequestBody NicknameRequestDto nicknameRequestDto,
                                            @PathVariable Long teamId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return teamService.changeTeamManager(nicknameRequestDto,teamId,userDetails);
    }
}
