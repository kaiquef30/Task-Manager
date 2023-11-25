package com.projectmicrosoft.microsoft.service;


import com.projectmicrosoft.microsoft.api.dto.ClientDTO;
import com.projectmicrosoft.microsoft.exception.ClientNotFoundException;
import com.projectmicrosoft.microsoft.model.Client;
import com.projectmicrosoft.microsoft.repository.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    public Optional<Client> getClientById(Long clientId) {
        return clientRepository.findById(clientId);
    }


    @Cacheable(value = "clientCache", key = "T(org.springframework.util.ClassUtils).getShortName('Client')")
    public List<Client> getAllClients() {
        return clientRepository.findAll();
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
