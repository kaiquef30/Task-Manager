package com.project.task.manager.api.resources.team;


import com.project.task.manager.dto.TeamDTO;
import com.project.task.manager.security.AuthenticatedUser;
import com.project.task.manager.exception.TeamAlreadyExistsException;
import com.project.task.manager.exception.TeamNotFoundException;
import com.project.task.manager.exception.UserIsAlreadyOnTheTeam;
import com.project.task.manager.exception.UserNotFoundException;
import com.project.task.manager.exception.messages.TeamMessagesConfig;
import com.project.task.manager.model.Team;
import com.project.task.manager.model.User;
import com.project.task.manager.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teams")
public class TeamResource {

    private final TeamService teamService;

    private final TeamMessagesConfig teamMessagesConfig;


    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "View all teams")
    @ApiResponse(responseCode = "200", description = "All teams returned.",
            content = {@Content(schema = @Schema(implementation = Team.class))})
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams(
            @PageableDefault(
                    sort = "name",
                    direction = Sort.Direction.ASC,
                    size = 3
            )Pageable page) {
        return ResponseEntity.ok(teamService.getAllTeams(page).stream().map(team ->
                team.add(linkTo(methodOn(TeamResource.class).getTeamById(team.getId()))
                        .withRel("Go to team"))
                ).toList());
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeamById(@PathVariable Long teamId) {
        return ResponseEntity.status(HttpStatus.OK).body(teamService.getTeamById(teamId)
                .add(linkTo(methodOn(TeamResource.class).getAllTeams(Pageable.unpaged()))
                        .withRel("Return to all teams")));
    }

    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "Create new team")
    @ApiResponse(responseCode = "201", description = "Team created successfully",
            content = {@Content(schema = @Schema(implementation = TeamDTO.class))})
    @ApiResponse(responseCode = "409", description = "Team already exists!", content = @Content)
    @PostMapping("/create")
    public ResponseEntity<?> createTeam(@AuthenticationPrincipal User authenticatedUser,
                                             @Valid @RequestBody TeamDTO teamDTO) {
        try {
            Team createdTeam;
            createdTeam = teamService.createTeam(teamDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
        } catch (TeamAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(teamMessagesConfig.getTeamAlreadyExists());
        }
    }


    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "Delete team")
    @ApiResponse(responseCode = "204", description = "Successfully deleted team.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Task not found.", content = @Content)
    @DeleteMapping("/delete/{teamId}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long teamId){
        try {
            teamService.deleteTeam(teamId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(teamMessagesConfig.getTeamDeletedSucessfully());
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(teamMessagesConfig.getTeamNotFound());
        }
    }


    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "Edit team")
    @ApiResponse(responseCode = "200", description = "Team information edited successfully.",
            content = {@Content(schema = @Schema(implementation = Team.class))})
    @ApiResponse(responseCode = "404", description = "Team not found!", content = @Content)
    @PutMapping("/edit/{teamId}")
    public ResponseEntity<?> updateTeam(@PathVariable Long teamId, @RequestBody TeamDTO updatedTeamDto) {
        try {
            Team updatedTeam = teamService.updateTeam(teamId, updatedTeamDto);
            return ResponseEntity.status(HttpStatus.OK).body(updatedTeam);
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(teamMessagesConfig.getTeamNotFound());
        }
    }


    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "Associate a user with a team")
    @ApiResponse(responseCode = "200", description = "User successfully added to team.", content = @Content)
    @ApiResponse(responseCode = "404", description = "User or Team not found!", content = @Content)
    @ApiResponse(responseCode = "409", description = "The user is already on the team", content = @Content)
    @PostMapping("/{teamId}/addUser/{userId}")
    public ResponseEntity<?> addUserToTeam(@PathVariable Long teamId, @PathVariable Long userId) {
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


    @AuthenticatedUser(requiredRoles = {"ADMIN"})
    @Operation(summary = "Remove user from team")
    @ApiResponse(responseCode = "200", description = "User successfully remove to team.", content = @Content)
    @ApiResponse(responseCode = "404", description = "User or Team not found!", content = @Content)
    @DeleteMapping("/{teamId}/removeUser/{userId}")
    public ResponseEntity<?> removeUserFromTeam(@PathVariable Long teamId, @PathVariable Long userId) {
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
