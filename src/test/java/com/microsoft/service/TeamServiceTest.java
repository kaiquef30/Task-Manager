package com.projectmicrosoft.microsoft.service;

import com.projectmicrosoft.microsoft.api.dto.TeamsDto;
import com.projectmicrosoft.microsoft.exception.TeamNotFoundException;
import com.projectmicrosoft.microsoft.exception.UserIsAlreadyOnTheTeam;
import com.projectmicrosoft.microsoft.exception.UserNotFoundException;
import com.projectmicrosoft.microsoft.model.Team;
import com.projectmicrosoft.microsoft.model.User;
import com.projectmicrosoft.microsoft.repository.TeamRepository;
import com.projectmicrosoft.microsoft.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TeamServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ModelMapper modelMapper;



    @InjectMocks
    private TeamService teamService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTeams() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team());
        teams.add(new Team());

        when(teamRepository.findAll()).thenReturn(teams);

        List<Team> resultTeams = teamService.getAllTeams();

        assertNotNull(resultTeams);
        assertEquals(2, resultTeams.size());
    }

    @Test
    public void testGetTeamById() throws TeamNotFoundException {
        Team team = new Team();
        team.setId(1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        Optional<Team> resultTeam = teamService.getTeamById(1L);

        assertTrue(resultTeam.isPresent());
        assertEquals(1L, resultTeam.get().getId());
    }

    @Test
    public void testUpdateTeam() throws TeamNotFoundException {
        TeamsDto updatedTeamDto = new TeamsDto();
        Team existingTeam = new Team();
        existingTeam.setId(1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(existingTeam));
        when(modelMapper.map(updatedTeamDto, Team.class)).thenReturn(existingTeam);
        when(teamRepository.save(existingTeam)).thenReturn(existingTeam);

        Team updatedTeam = teamService.updateTeam(1L, updatedTeamDto);

        assertNotNull(updatedTeam);
        assertEquals(1L, updatedTeam.getId());
    }

    @Test
    public void testUpdateTeamTeamNotFoundException() {
        TeamsDto updatedTeamDto = new TeamsDto();

        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> teamService.updateTeam(1L, updatedTeamDto));
    }

    @Test
    public void testDeleteTeam() throws TeamNotFoundException {
        when(teamRepository.existsById(1L)).thenReturn(true);

        teamService.deleteTeam(1L);

        verify(teamRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTeamTeamNotFoundException() {
        when(teamRepository.existsById(1L)).thenReturn(false);

        assertThrows(TeamNotFoundException.class, () -> teamService.deleteTeam(1L));
    }

    @Test
    public void testAddUserToTeam() throws UserNotFoundException, TeamNotFoundException, UserIsAlreadyOnTheTeam {
        Team team = new Team();
        team.setId(1L);
        User user = new User();
        user.setId(1L);

        if (team.getUsers() == null) {
            team.setUsers(new ArrayList<>());
        }

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        teamService.addUserToTeam(1L, 1L);

        assertTrue(team.getUsers().contains(user));
        verify(teamRepository, times(1)).save(team);
    }


    @Test
    public void testRemoveUserFromTeam() throws UserNotFoundException, TeamNotFoundException {
        Team team = new Team();
        team.setId(1L);
        User user = new User();
        user.setId(1L);

        team.setUsers(new ArrayList<>());

        team.getUsers().add(user);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        teamService.removeUserFromTeam(1L, 1L);

        assertFalse(team.getUsers().contains(user));
        verify(teamRepository, times(1)).save(team);
    }


}
