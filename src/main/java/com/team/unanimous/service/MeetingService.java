package com.team.unanimous.service;

import com.team.unanimous.dto.requestDto.MeetingRequestDto;
import com.team.unanimous.dto.responseDto.MeetingResponseDto;
import com.team.unanimous.exceptionHandler.CustomException;
import com.team.unanimous.exceptionHandler.ErrorCode;
import com.team.unanimous.model.meeting.Meeting;
import com.team.unanimous.model.meeting.MeetingUser;
import com.team.unanimous.model.team.Team;
import com.team.unanimous.model.user.User;
import com.team.unanimous.repository.meeting.MeetingRepository;
import com.team.unanimous.repository.meeting.MeetingUserRepository;
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
public class MeetingService {

    private final UserRepository userRepository;

    private final TeamRepository teamRepository;

    private final MeetingRepository meetingRepository;

    private final MeetingUserRepository meetingUserRepository;

    // 미팅 예약하기 생성
    @Transactional
    public ResponseEntity createMeeting(MeetingRequestDto meetingRequestDto,
                                        UserDetailsImpl userDetails,
                                        Long teamId){
        String meetingTitle = meetingRequestDto.getMeetingTitle().trim();
        if (meetingTitle.length() > 20){
            throw new CustomException(ErrorCode.MEETING_NAME_LENGTH);
        } else if (meetingTitle.isEmpty()){
            throw new CustomException(ErrorCode.MEETING_NAME_LENGTH);
        } else if (meetingTitle.equals("")){
            throw new CustomException(ErrorCode.MEETING_NAME_LENGTH);
        } else if (meetingTitle == null){
            throw new CustomException(ErrorCode.MEETING_NAME_LENGTH);
        }
        String meetingDate = meetingRequestDto.getMeetingDate();
        String meetingTime = meetingRequestDto.getMeetingTime();
        String meetingDuration = meetingRequestDto.getMeetingDuration();
        String[] meetingTime1 = meetingTime.split(":");
        String[] meetingDuration1 = meetingDuration.split("시");
        String meetingTime2 = meetingTime1[0];
        String meetingDuration2 = meetingDuration1[0];
        String meetingOverTime1 = "";

        int meetingTimeInt = Integer.parseInt(meetingTime2);
        int meetingDurationInt = Integer.parseInt(meetingDuration2);
        int meetingOverTime = meetingTimeInt + meetingDurationInt;

        if (meetingOverTime >= 24){
            meetingOverTime -= 24;
        }

        if (meetingOverTime < 10){
            meetingOverTime1 = "0"+meetingOverTime+":00";
        } else {
            meetingOverTime1 = meetingOverTime+":00";
        }


        User user = userRepository.findUserById(userDetails.getUser().getId());
        if (user == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        Team team = teamRepository.findTeamById(teamId);
        if (team == null){
            throw new CustomException(ErrorCode.TEAM_NOT_FOUND);
        }


        Meeting meeting = Meeting.builder()
                .meetingStatus(Meeting.Status.YET)
                .meetingTitle(meetingTitle)
                .meetingDate(meetingDate)
                .meetingTime(meetingTime)
                .meetingSum(meetingRequestDto.getMeetingSum())
                .meetingTheme(meetingRequestDto.getMeetingTheme())
                .meetingDuration(meetingDuration)
                .meetingOverTime(meetingOverTime1)
                .meetingCreator(user)
                .team(team)
                .build();
        meetingRepository.save(meeting);

        MeetingUser meetingUser = new MeetingUser(user,meeting);

        meetingUserRepository.save(meetingUser);
        Long meetingId = meeting.getId();
        return ResponseEntity.ok(meetingId);
    }

    // 미팅 바로 시작하기
    public ResponseEntity createMeetingNow(MeetingRequestDto meetingRequestDto,
                                           UserDetailsImpl userDetails,
                                           Long teamId){
        String meetingTitle = meetingRequestDto.getMeetingTitle().trim();
        if (meetingTitle.length() > 20){
            throw new CustomException(ErrorCode.MEETING_NAME_LENGTH);
        } else if (meetingTitle.isEmpty()){
            throw new CustomException(ErrorCode.MEETING_NAME_LENGTH);
        } else if (meetingTitle.equals("")){
            throw new CustomException(ErrorCode.MEETING_NAME_LENGTH);
        } else if (meetingTitle == null){
            throw new CustomException(ErrorCode.MEETING_NAME_LENGTH);
        }
        User user = userRepository.findUserById(userDetails.getUser().getId());
        if (user == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        Team team = teamRepository.findTeamById(teamId);
        if (team == null){
            throw new CustomException(ErrorCode.TEAM_NOT_FOUND);
        }

        Meeting meeting = Meeting.builder()
                .meetingStatus(Meeting.Status.NOW)
                .meetingTitle(meetingTitle)
                .meetingDate(meetingRequestDto.getMeetingDate())
                .meetingTime(meetingRequestDto.getMeetingTime())
                .meetingSum(meetingRequestDto.getMeetingSum())
                .meetingTheme(meetingRequestDto.getMeetingTheme())
                .meetingCreator(user)
                .team(team)
                .build();
        meetingRepository.save(meeting);

        MeetingUser meetingUser = new MeetingUser(user,meeting);

        meetingUserRepository.save(meetingUser);

        Long meetingId = meeting.getId();
        return ResponseEntity.ok(meetingId);
    }

    // 미팅 목록
    public List<MeetingResponseDto> getMeetings(Long teamId){
        List<Meeting> meetingList = meetingRepository.findAllByTeamId(teamId);

        List<MeetingResponseDto> meetingResponseDtos = new ArrayList<>();
        for (Meeting meeting : meetingList){
            User user = meeting.getMeetingCreator();
            MeetingResponseDto meetingResponseDto = new MeetingResponseDto(meeting,user);
            meetingResponseDtos.add(meetingResponseDto);
        }
        return meetingResponseDtos;
    }

    // 예약 중인 미팅 목록
    public List<MeetingResponseDto> getYetMeetings(Long teamId){
        List<Meeting> meetingList = meetingRepository.findAllByOrderByModifiedAtDesc();
        List<Meeting> meetingList1 = new ArrayList<>();
        for (Meeting meeting : meetingList){
            if (meeting.getTeam().getId().equals(teamId)){
                meetingList1.add(meeting);
            }
        }

        List<MeetingResponseDto> meetingResponseDtos = new ArrayList<>();
        for (Meeting meeting : meetingList1){
            if (meeting.getMeetingStatus().equals(Meeting.Status.YET)) {
                User user = meeting.getMeetingCreator();
                MeetingResponseDto meetingResponseDto = new MeetingResponseDto(meeting,user);
                meetingResponseDtos.add(meetingResponseDto);
            }
        }
        return meetingResponseDtos;
    }

    // 진행중인 미팅 목록
    public List<MeetingResponseDto> getNowMeetings(Long teamId){
        List<Meeting> meetingList = meetingRepository.findAllByOrderByModifiedAtDesc();
        List<Meeting> meetingList1 = new ArrayList<>();
        for (Meeting meeting : meetingList){
            if (meeting.getTeam().getId().equals(teamId)){
                meetingList1.add(meeting);
            }
        }


        List<MeetingResponseDto> meetingResponseDtos = new ArrayList<>();
        for (Meeting meeting : meetingList1){
            if (meeting.getMeetingStatus().equals(Meeting.Status.NOW)) {
                User user = meeting.getMeetingCreator();
                MeetingResponseDto meetingResponseDto = new MeetingResponseDto(meeting,user);
                meetingResponseDtos.add(meetingResponseDto);
            }
        }
        return meetingResponseDtos;
    }

    // 이전 미팅 목록
    public List<MeetingResponseDto> getDoneMeetings(Long teamId){
        List<Meeting> meetingList = meetingRepository.findAllByOrderByModifiedAtDesc();
        List<Meeting> meetingList1 = new ArrayList<>();
        for (Meeting meeting : meetingList){
            if (meeting.getTeam().getId().equals(teamId)){
                meetingList1.add(meeting);
            }
        }

        List<MeetingResponseDto> meetingResponseDtos = new ArrayList<>();
        for (Meeting meeting : meetingList1){
            if (meeting.getMeetingStatus().equals(Meeting.Status.DONE)) {
                User user = meeting.getMeetingCreator();
                MeetingResponseDto meetingResponseDto = new MeetingResponseDto(meeting,user);
                meetingResponseDtos.add(meetingResponseDto);
            }
        }
        return meetingResponseDtos;
    }

    // 특정 미팅 조회
    public MeetingResponseDto getMeeting(Long meetingId){
        Meeting meeting = meetingRepository.findMeetingById(meetingId);
        if (meeting == null){
            throw new CustomException(ErrorCode.MEETING_NOT_FOUND);
        }
        User user = meeting.getMeetingCreator();

        MeetingResponseDto meetingResponseDto = new MeetingResponseDto(meeting,user);
        return meetingResponseDto;
    }

    // 미팅 프로필 수정
    @Transactional
    public ResponseEntity updateMeeting(MeetingRequestDto requestDto,
                                        Long meetingId,
                                        UserDetailsImpl userDetails){
        Meeting meeting = meetingRepository.findMeetingById(meetingId);
        if (!(meeting.getMeetingCreator().getNickname().equals(userDetails.getUser().getNickname()))){
            throw new CustomException(ErrorCode.INVALID_AUTHORITY);
        }

        String meetingTitle = requestDto.getMeetingTitle();
        if (meetingTitle.length() > 20){
            throw new CustomException(ErrorCode.MEETING_NAME_LENGTH);
        } else if (meetingTitle.isEmpty()){
            throw new CustomException(ErrorCode.MEETING_NAME_LENGTH);
        } else if (meetingTitle.equals("")){
            throw new CustomException(ErrorCode.MEETING_NAME_LENGTH);
        } else if (meetingTitle == null){
            throw new CustomException(ErrorCode.MEETING_NAME_LENGTH);
        }

        meeting.updateMeeting(requestDto);
        meetingRepository.save(meeting);
        return ResponseEntity.ok("미팅 정보 수정 완료");
    }

    // 미팅 삭제
    @Transactional
    public ResponseEntity deleteMeeting(Long meetingId,
                                        UserDetailsImpl userDetails){
        Meeting meeting = meetingRepository.findMeetingById(meetingId);
        if (!(meeting.getMeetingCreator().getNickname().equals(userDetails.getUser().getNickname()))){
            throw new CustomException(ErrorCode.INVALID_AUTHORITY);
        }

        meetingRepository.delete(meeting);
        return ResponseEntity.ok("미팅 삭제 완료");
    }
}
