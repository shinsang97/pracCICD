package com.team.unanimous.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByRefreshToken(String token);
}