package com.project.task.manager.api.resources.auth;


import com.project.task.manager.dto.LoginBody;
import com.project.task.manager.dto.LoginResponse;
import com.project.task.manager.dto.PasswordResetBody;
import com.project.task.manager.dto.RegistrationBody;
import com.project.task.manager.exception.EmailFailureException;
import com.project.task.manager.exception.EmailNotFoundException;
import com.project.task.manager.exception.InvalidCredentialsException;
import com.project.task.manager.exception.messages.AuthenticationMessageConfig;
import com.project.task.manager.model.User;
import com.project.task.manager.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationResource {

    private final AuthenticationService authenticationService;

    private final AuthenticationMessageConfig authenticationMessageConfig;


    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "200", description = "Registration completed successfully.",
            content = {@Content(schema = @Schema(implementation = RegistrationBody.class))})
    @ApiResponse(responseCode = "409", description = "Email already registered, please try another one.",
            content = @Content)
    @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(authenticationService.registerUser(registrationBody));
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(authenticationMessageConfig.getInternalServerError());
        }
        catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(authenticationMessageConfig.getUserAlreadyExists());
        }
    }

    @Operation(summary = "Log in")
    @ApiResponse(responseCode = "200", description = "Login successfully.",
            content = {@Content(schema = @Schema(implementation = LoginResponse.class))})
    @ApiResponse(responseCode = "403", description = "Unverified email.", content = @Content)
    @ApiResponse(responseCode = "404", description = "unregistered email.", content = @Content)
    @ApiResponse(responseCode = "401", description = "Invalid credentials.", content = @Content)
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginBody loginBody) {
        LoginResponse response;
        try {
            response = authenticationService.loginUser(loginBody);
            return ResponseEntity.ok(response);
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(authenticationMessageConfig.getEmailNotVerified());
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(authenticationMessageConfig.getInvalidCredentials());
        }
    }


    @Operation(summary = "Method to verify a user")
    @ApiResponse(responseCode = "200", description = "Email verified successfully.", content = @Content)
    @ApiResponse(responseCode = "409", description = "Email already verified.", content = @Content)
    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        if (authenticationService.verifyUser(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(authenticationMessageConfig.getEmailAlreadyVerified());
        }
    }

    @Operation(summary = "method to check logged in user information")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class))})
    @GetMapping("/me")
    public ResponseEntity<User> getLoggedInUserProfile(@AuthenticationPrincipal User user, @RequestParam String token){
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Forgot password")
    @ApiResponse(responseCode = "200", description = "Email sent to reset password.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Email not registered.", content = @Content)
    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            authenticationService.forgotPassword(email);
            return ResponseEntity.status(HttpStatus.OK).body(authenticationMessageConfig.getForgotPasswordOk());
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(authenticationMessageConfig.getInvalidCredentials());
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(authenticationMessageConfig.getErrorSendingVerificationEmail());
        }
    }

    @Operation(summary = "method to reset password")
    @ApiResponse(responseCode = "200", description = "Password reset successfully", content = {@Content(schema =
    @Schema(implementation = PasswordResetBody.class))})
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetBody passwordResetBody) {
        authenticationService.resetPassword(passwordResetBody);
        return ResponseEntity.ok().build();
    }
}
