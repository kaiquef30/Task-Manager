package com.microsoft.service;

import com.projectmicrosoft.microsoft.api.DTO.ClientDTO;
import com.projectmicrosoft.microsoft.exception.ClientNotFoundException;
import com.projectmicrosoft.microsoft.model.Client;
import com.projectmicrosoft.microsoft.repository.ClientRepository;
import com.projectmicrosoft.microsoft.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

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
    public void testRegisterClient() {
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
    public void testGetClientById() {
        Long clientId = 1L;
        Client client = new Client();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.getClientById(clientId);

        assertTrue(result.isPresent());
        assertEquals(client, result.get());

        verify(clientRepository).findById(clientId);
    }

    @Test
    public void testGetAllClients() {
        List<Client> clients = List.of(new Client(), new Client());

        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> result = clientService.getAllClients();

        assertEquals(clients, result);

        verify(clientRepository).findAll();
    }

    @Test
    public void testUpdateClient() throws ClientNotFoundException {
        Long clientId = 1L;
        ClientDTO clientDto = new ClientDTO();
        Client existingClient = new Client();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));
//        when(modelMapper.map(clientDto, existingClient)).thenReturn(existingClient);
        when(clientRepository.save(existingClient)).thenReturn(existingClient);

        Client result = clientService.updateClient(clientId, clientDto);

        assertNotNull(result);
        assertEquals(existingClient, result);

        verify(clientRepository).findById(clientId);
        verify(modelMapper).map(clientDto, existingClient);
        verify(clientRepository).save(existingClient);
    }

    @Test
    public void testUpdateClient_ClientNotFoundException() {
        Long clientId = 1L;
        ClientDTO clientDto = new ClientDTO();

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.updateClient(clientId, clientDto));

        verify(clientRepository).findById(clientId);
        verify(modelMapper, never()).map(eq(clientDto), any());
        verify(clientRepository, never()).save(any());
    }


    @Test
    public void testDeleteClient() {
        Long clientId = 1L;

        when(clientRepository.existsById(clientId)).thenReturn(true);

        assertDoesNotThrow(() -> clientService.deleteClient(clientId));

        verify(clientRepository).existsById(clientId);
        verify(clientRepository).deleteById(clientId);
    }

    @Test
    public void testDeleteClient_ClientNotFoundException() {
        Long clientId = 1L;

        when(clientRepository.existsById(clientId)).thenReturn(false);

        assertThrows(ClientNotFoundException.class, () -> clientService.deleteClient(clientId));

        verify(clientRepository).existsById(clientId);
        verify(clientRepository, never()).deleteById(any());
    }
}