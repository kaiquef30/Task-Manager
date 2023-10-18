package com.projectmicrosoft.microsoft.repository;

import com.projectmicrosoft.microsoft.model.User;
import com.projectmicrosoft.microsoft.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    Object deleteByUser(User user);

}
