package com.team.unanimous.email;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {

    Optional<EmailCode> findByUsername(String username);
    Optional<EmailCode> findByCode(String code);
}
