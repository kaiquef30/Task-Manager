package com.projectmicrosoft.microsoft.api.controller.teams;


import com.projectmicrosoft.microsoft.api.dto.TeamsDto;
import com.projectmicrosoft.microsoft.api.security.AuthenticatedUser;
import com.projectmicrosoft.microsoft.exception.TeamAlreadyExistsException;
import com.projectmicrosoft.microsoft.exception.TeamNotFoundException;
import com.projectmicrosoft.microsoft.exception.UserIsAlreadyOnTheTeam;
import com.projectmicrosoft.microsoft.exception.UserNotFoundException;
import com.projectmicrosoft.microsoft.exception.messages.TeamMessagesConfig;
import com.projectmicrosoft.microsoft.model.Team;
import com.projectmicrosoft.microsoft.model.User;
import com.projectmicrosoft.microsoft.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamsController {

    private final TeamService teamService;

    private final TeamMessagesConfig teamMessagesConfig;

    public TeamsController(TeamService teamService, TeamMessagesConfig teamMessagesConfig) {
        this.teamService = teamService;
        this.teamMessagesConfig = teamMessagesConfig;
    }


    @AuthenticatedUser(requiredRoles = "USER")
    @GetMapping
    public List<Team> getAllTeams(@AuthenticationPrincipal User authenticatedUser) {
        return teamService.getAllTeams();
    }



    @Operation(summary = "Create new team")
    @ApiResponse(responseCode = "201", description = "Team created successfully",
            content = {@Content(schema = @Schema(implementation = TeamsDto.class))})
    @ApiResponse(responseCode = "409", description = "Team already exists!", content = @Content)
    @ApiResponse(responseCode = "403", description = "You are not allowed to create a new team", content = @Content)
    @AuthenticatedUser(requiredRoles = "ADMIN")
    @PostMapping("/create")
    public ResponseEntity<?> createTeam(@AuthenticationPrincipal User authenticatedUser,
                                             @Valid @RequestBody TeamsDto teamsDto) {
        try {
            teamService.createTeam(teamsDto);
            return ResponseEntity.status(HttpStatus.OK).body(teamMessagesConfig.getTeamCreatedSuccessfully());
        } catch (TeamAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(teamMessagesConfig.getTeamAlreadyExists());
        }
    }


    @AuthenticatedUser(requiredRoles = "ADMIN")
    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long teamId,
                           @AuthenticationPrincipal User authenticatedUser){
        try {
            teamService.deleteTeam(teamId);
            return ResponseEntity.status(HttpStatus.OK).body(teamMessagesConfig.getTeamDeletedSucessfully());
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(teamMessagesConfig.getTeamNotFound());
        }
    }

    @ApiResponse(responseCode = "200", description = "User successfully added to team.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Team not found!", content = @Content)
    @AuthenticatedUser(requiredRoles = "ADMIN")
    @PutMapping("/{teamId}")
    public ResponseEntity<?> updateTeam(@PathVariable Long teamId, @RequestBody TeamsDto updatedTeamDto,
                           @AuthenticationPrincipal User authenticatedUser) {
        try {
            teamService.updateTeam(teamId, updatedTeamDto);
            return ResponseEntity.status(HttpStatus.OK).body(teamMessagesConfig.getTeamEditedSucessfully());
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(teamMessagesConfig.getTeamNotFound());
        }
    }

    @Operation(summary = "Associate a user with a team")
    @ApiResponse(responseCode = "200", description = "User successfully added to team.", content = @Content)
    @ApiResponse(responseCode = "404", description = "User or Team not found!", content = @Content)
    @ApiResponse(responseCode = "409", description = "The user is already on the team", content = @Content)
    @AuthenticatedUser(requiredRoles = "ADMIN")
    @PostMapping("/{teamId}/addUser/{userId}")
    public ResponseEntity<?> addUserToTeam(@PathVariable Long teamId, @PathVariable Long userId,
                              @AuthenticationPrincipal User authenticatedUser) {
        try {
            teamService.addUserToTeam(teamId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(teamMessagesConfig.getUserAddedSuccessfully());
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(teamMessagesConfig.getTeamNotFound());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(teamMessagesConfig.getUserNotFound());
        } catch (UserIsAlreadyOnTheTeam e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(teamMessagesConfig.getUserIsAlreadyOnTheTeam());
        }
    }

    @AuthenticatedUser(requiredRoles = "ADMIN")
    @DeleteMapping("/{teamId}/removeUser/{userId}")
    public ResponseEntity<?> removeUserFromTeam(@PathVariable Long teamId, @PathVariable Long userId,
                                   @AuthenticationPrincipal User authenticatedUser) {
        try {
            teamService.removeUserFromTeam(teamId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(teamMessagesConfig.getUserRemovedSuccessfully());
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(teamMessagesConfig.getTeamNotFound());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(teamMessagesConfig.getUserNotFound());
        }
    }
}
