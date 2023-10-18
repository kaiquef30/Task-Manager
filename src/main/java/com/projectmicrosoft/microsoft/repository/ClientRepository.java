package com.projectmicrosoft.microsoft.repository;

import com.projectmicrosoft.microsoft.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
