package com.team.unanimous.repository;

import com.team.unanimous.model.TeamImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamImageRepository extends JpaRepository<TeamImage, Long> {

    TeamImage findByTeamImageId(Long teamImageId);

}
