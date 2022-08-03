package com.team.unanimous.repository.user;

import com.team.unanimous.model.meeting.Meeting;
import com.team.unanimous.model.team.Team;
import com.team.unanimous.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

//    List<User> findAllById(Team team);

    User findUserById(Long id);

//    List<User> findAllById(Meeting meeting);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByUsername(String username);

//    Optional<User> findByKakaoId(Long kakaoId);

    User findUserByNickname(String nickname);

    User findUserByUsername(String username);

}
