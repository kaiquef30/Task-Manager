package com.project.task.manager.service;

import com.project.task.manager.dto.TeamDTO;
import com.project.task.manager.exception.TeamAlreadyExistsException;
import com.project.task.manager.exception.TeamNotFoundException;
import com.project.task.manager.exception.UserIsAlreadyOnTheTeam;
import com.project.task.manager.exception.UserNotFoundException;
import com.project.task.manager.repository.TeamRepository;
import com.project.task.manager.repository.UserRepository;
import com.project.task.manager.model.Team;
import com.project.task.manager.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final UserRepository userRepository;

    private final TeamRepository teamRepository;

    private final ModelMapper modelMapper;


    @Cacheable(value = "teamCache", key = "T(org.springframework.util.ClassUtils).getShortName('Team')")
    public List<Team> getAllTeams(Pageable page) {
        return teamRepository.findAll();
    }


    @Cacheable(value = "teamCache", key = "#teamId")
    public Team getTeamById(Long teamId) {
       return teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
    }

    @Transactional
    @CacheEvict(value = "teamCache", allEntries = true)
    public Team createTeam(TeamDTO teamDTO) throws TeamAlreadyExistsException {
        getTeamNameOrThrow(teamDTO.getName());
        Team team = modelMapper.map(teamDTO, Team.class);
        Long currentUserId = getCurrentUserId();
        team.setCreator(currentUserId);
        return teamRepository.save(team);
    }

    @Transactional
    @CacheEvict(value = "teamCache", key = "#teamId")
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
    @CacheEvict(value = "teamCache", key = "#teamId")
    public void deleteTeam(Long teamId) throws TeamNotFoundException {
        if (teamRepository.existsById(teamId)) {
            teamRepository.deleteById(teamId);
        } else {
            throw new TeamNotFoundException();
        }
    }

    @Transactional
    public void addUserToTeam(Long teamId, Long userId) throws TeamNotFoundException, UserNotFoundException,
            UserIsAlreadyOnTheTeam {
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
