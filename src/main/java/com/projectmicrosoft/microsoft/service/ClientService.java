package com.projectmicrosoft.microsoft.service;


import com.projectmicrosoft.microsoft.api.dto.ClientDto;
import com.projectmicrosoft.microsoft.exception.TeamNotFoundException;
import com.projectmicrosoft.microsoft.model.Client;
import com.projectmicrosoft.microsoft.repository.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;


    public ClientService(ClientRepository clientRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }


    public Client registerClient(ClientDto clientDto) {
        Client client = modelMapper.map(clientDto, Client.class);
        return clientRepository.save(client);
    }


    public Optional<Client> getClientById(Long clientId) {
        return clientRepository.findById(clientId);
    }


    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client updateClient(Long clientId, ClientDto clientDto) throws TeamNotFoundException {
        Optional<Client> existingClientOptional = clientRepository.findById(clientId);

        if (existingClientOptional.isPresent()) {
            Client existingClient = existingClientOptional.get();
            modelMapper.map(clientDto, existingClient);
            return clientRepository.save(existingClient);
        }

        throw new TeamNotFoundException();
    }

    public void deleteClient(Long clientId) {
        clientRepository.deleteById(clientId);
    }

}
