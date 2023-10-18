package com.projectmicrosoft.microsoft.repository;

import com.projectmicrosoft.microsoft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
}
