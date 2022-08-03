package com.team.unanimous.repository.meeting;

import com.team.unanimous.model.meeting.Meeting;
import com.team.unanimous.model.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {


    List<Meeting> findAllByTeamId(Long teamId);

    Meeting findMeetingById(Long meetingId);

    List<Meeting> findAllByOrderByModifiedAtDesc();

}
