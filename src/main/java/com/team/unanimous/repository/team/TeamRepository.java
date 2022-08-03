package com.team.unanimous.repository.team;

import com.team.unanimous.model.team.Team;
import com.team.unanimous.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team,Long> {

    Team findByUuid(String uuid);

    Team findTeamById(Long id);

}
