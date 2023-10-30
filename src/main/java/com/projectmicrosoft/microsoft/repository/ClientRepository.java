package com.projectmicrosoft.microsoft.repository;

import com.projectmicrosoft.microsoft.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByCpf(String cpf);
}
