package com.projectmicrosoft.microsoft.service;

import com.projectmicrosoft.microsoft.api.DTO.TeamDTO;
import com.projectmicrosoft.microsoft.exception.TeamAlreadyExistsException;
import com.projectmicrosoft.microsoft.exception.TeamNotFoundException;
import com.projectmicrosoft.microsoft.exception.UserIsAlreadyOnTheTeam;
import com.projectmicrosoft.microsoft.exception.UserNotFoundException;
import com.projectmicrosoft.microsoft.model.Team;
import com.projectmicrosoft.microsoft.model.User;
import com.projectmicrosoft.microsoft.repository.TeamRepository;
import com.projectmicrosoft.microsoft.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final UserRepository userRepository;

    private final TeamRepository teamRepository;

    private final ModelMapper modelMapper;

    public TeamService(UserRepository userRepository, TeamRepository teamRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
    }


    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Optional<Team> getTeamById(Long teamId) {
        if (teamRepository.existsById(teamId)) {
            return teamRepository.findById(teamId);
        }
        throw new TeamNotFoundException();
    }


    @Transactional
    public Team createTeam(TeamDTO teamDTO) throws TeamAlreadyExistsException {
        getTeamNameOrThrow(teamDTO.getName());
        Team team = modelMapper.map(teamDTO, Team.class);
        Long currentUserId = getCurrentUserId();
        team.setCreator(currentUserId);
        return teamRepository.save(team);
    }

    @Transactional
    public Team updateTeam(Long teamId, TeamDTO updatedTeamDto) throws TeamNotFoundException {
        Optional<Team> existingTeamOptional = teamRepository.findById(teamId);
        if (existingTeamOptional.isPresent()) {
            Team existingTeam = existingTeamOptional.get();
            modelMapper.map(updatedTeamDto, existingTeam);
            return teamRepository.save(existingTeam);
        }
        throw new TeamNotFoundException();
    }


    @Transactional
    public void deleteTeam(Long teamId) throws TeamNotFoundException {
        if (teamRepository.existsById(teamId)) {
            teamRepository.deleteById(teamId);
        } else {
            throw new TeamNotFoundException();
        }
    }


    @Transactional
    public void addUserToTeam(Long teamId, Long userId) throws TeamNotFoundException, UserNotFoundException, UserIsAlreadyOnTheTeam {
        Team team = getTeamOrThrow(teamId);
        User user = getUserOrThrow(userId);

        if (!team.getUsers().contains(user)) {
            team.getUsers().add(user);
            team.setNumbersOfUsers(team.getNumbersOfUsers() + 1);
            teamRepository.save(team);
        } else {
            throw new UserIsAlreadyOnTheTeam();
        }
    }


    @Transactional
    public void removeUserFromTeam(Long teamId, Long userId) throws TeamNotFoundException, UserNotFoundException {
        Team team = getTeamOrThrow(teamId);
        User user = getUserOrThrow(userId);

        if (team.getUsers().contains(user)) {
            team.getUsers().remove(user);
            teamRepository.save(team);
        }
    }


    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((User) principal).getId();
        } else {
            throw new IllegalStateException();
        }
    }

    private Team getTeamOrThrow(Long teamId) throws TeamNotFoundException {
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        if (teamOptional.isPresent()) {
            return teamOptional.get();
        } else {
            throw new TeamNotFoundException();
        }
    }

    private Team getTeamNameOrThrow(String teamName) throws TeamAlreadyExistsException {
        Optional<Team> existingTeam = teamRepository.findByNameIgnoreCase(teamName);
        if (existingTeam.isPresent()) {
            throw new TeamAlreadyExistsException();
        }
        return new Team();
    }

    private User getUserOrThrow(Long userId) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException();
        }
    }
}