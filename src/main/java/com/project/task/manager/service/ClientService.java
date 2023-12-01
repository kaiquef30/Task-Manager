package com.project.task.manager.service;


import com.project.task.manager.dto.ClientDTO;
import com.project.task.manager.exception.ClientNotFoundException;
import com.project.task.manager.model.Client;
import com.project.task.manager.repository.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final ModelMapper modelMapper;


    @Transactional
    @CacheEvict(value = "clientCache", allEntries = true)
    public Client registerClient(ClientDTO clientDto) {
        Client client = modelMapper.map(clientDto, Client.class);
        return clientRepository.save(client);
    }


    @Cacheable(value = "clientCache", key = "#clientId")
    public Client getClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(ClientNotFoundException::new);
    }


    @Cacheable(value = "clientCache", key = "T(org.springframework.util.ClassUtils).getShortName('Client')")
    public List<Client> getAllClients(Pageable page) {
        return clientRepository.findAll(page).getContent();
    }

    @Transactional
    @CacheEvict(value = "clientCache", key = "#clientId")
    public Client updateClient(Long clientId, ClientDTO clientDto) throws ClientNotFoundException {
        Optional<Client> existingClientOptional = clientRepository.findById(clientId);

        if (existingClientOptional.isPresent()) {
            Client existingClient = existingClientOptional.get();
            modelMapper.map(clientDto, existingClient);
            return clientRepository.save(existingClient);
        }

        throw new ClientNotFoundException();
    }

    @Transactional
    @CacheEvict(value = "clientCache", key = "#clientId")
    public void deleteClient(Long clientId) throws ClientNotFoundException {
        if (clientRepository.existsById(clientId)) {
            clientRepository.deleteById(clientId);
        } else {
            throw new ClientNotFoundException();
        }
    }

}
