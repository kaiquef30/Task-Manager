package com.microsoft.service;

import com.project.task.manager.dto.ClientDTO;
import com.project.task.manager.exception.ClientNotFoundException;
import com.project.task.manager.model.Client;
import com.project.task.manager.repository.ClientRepository;
import com.project.task.manager.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class ClientServiceTest {


    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ModelMapper modelMapper;

    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        clientService = new ClientService(clientRepository, modelMapper);
    }

    @Test
     void testRegisterClient() {
        var clientDto = new ClientDTO();
        var client = new Client();

        when(modelMapper.map(clientDto, Client.class)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);

        Client result = clientService.registerClient(clientDto);

        assertNotNull(result);
        assertEquals(client, result);

        verify(modelMapper).map(clientDto, Client.class);
        verify(clientRepository).save(client);
    }

    @Test
     void testGetClientById() {
        Long clientId = 1L;
        Client client = new Client();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        Client result = clientService.getClientById(clientId);

        assertNotNull(result);
        assertEquals(client, result);

        verify(clientRepository).findById(clientId);
    }

    @Test
    void testGetAllClients() {
        List<Client> clients = List.of(new Client(), new Client());
        Pageable pageable = PageRequest.of(0, 10);

        when(clientRepository.findAll(pageable)).thenReturn(new PageImpl<>(clients));

        List<Client> result = clientService.getAllClients(pageable);

        assertEquals(clients, result);

        verify(clientRepository).findAll(pageable);
    }

    @Test
     void testUpdateClient() throws ClientNotFoundException {
        Long clientId = 1L;
        ClientDTO clientDto = new ClientDTO();
        Client existingClient = new Client();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(existingClient)).thenReturn(existingClient);

        Client result = clientService.updateClient(clientId, clientDto);

        assertNotNull(result);
        assertEquals(existingClient, result);

        verify(clientRepository).findById(clientId);
        verify(modelMapper).map(clientDto, existingClient);
        verify(clientRepository).save(existingClient);
    }

    @Test
     void testUpdateClient_ClientNotFoundException() {
        Long clientId = 1L;
        ClientDTO clientDto = new ClientDTO();

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.updateClient(clientId, clientDto));

        verify(clientRepository).findById(clientId);
        verify(modelMapper, never()).map(eq(clientDto), any());
        verify(clientRepository, never()).save(any());
    }


    @Test
     void testDeleteClient() {
        Long clientId = 1L;

        when(clientRepository.existsById(clientId)).thenReturn(true);

        assertDoesNotThrow(() -> clientService.deleteClient(clientId));

        verify(clientRepository).existsById(clientId);
        verify(clientRepository).deleteById(clientId);
    }

    @Test
     void testDeleteClient_ClientNotFoundException() {
        Long clientId = 1L;

        when(clientRepository.existsById(clientId)).thenReturn(false);

        assertThrows(ClientNotFoundException.class, () -> clientService.deleteClient(clientId));

        verify(clientRepository).existsById(clientId);
        verify(clientRepository, never()).deleteById(any());
    }

}
