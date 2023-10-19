package com.projectmicrosoft.microsoft.api.controller.auth;


import com.projectmicrosoft.microsoft.api.dto.LoginBody;
import com.projectmicrosoft.microsoft.api.dto.LoginResponse;
import com.projectmicrosoft.microsoft.api.dto.PasswordResetBody;
import com.projectmicrosoft.microsoft.api.dto.RegistrationBody;
import com.projectmicrosoft.microsoft.exception.EmailFailureException;
import com.projectmicrosoft.microsoft.exception.EmailNotFoundException;
import com.projectmicrosoft.microsoft.exception.InvalidCredentialsException;
import com.projectmicrosoft.microsoft.exception.UserAlreadyExistsException;
import com.projectmicrosoft.microsoft.exception.messages.AuthenticationMessageConfig;
import com.projectmicrosoft.microsoft.model.User;
import com.projectmicrosoft.microsoft.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final AuthenticationMessageConfig authenticationMessageConfig;

    public AuthenticationController(AuthenticationService authenticationService, AuthenticationMessageConfig authenticationMessageConfig) {
        this.authenticationService = authenticationService;
        this.authenticationMessageConfig = authenticationMessageConfig;
    }


    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "200", description = "Registration completed successfully.",
            content = {@Content(schema = @Schema(implementation = RegistrationBody.class))})
    @ApiResponse(responseCode = "409", description = "Email already registered, please try another one.", content = @Content)
    @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(authenticationService.registerUser(registrationBody));
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(authenticationMessageConfig.getEmailAlreadyRegistered());
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(authenticationMessageConfig.getInternalServerError());
        }
    }

    @Operation(summary = "Log in")
    @ApiResponse(responseCode = "200", description = "Login successfully.",
            content = {@Content(schema = @Schema(implementation = LoginBody.class))})
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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(authenticationMessageConfig.getEmailNotVerified());
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(authenticationMessageConfig.getEmailInvalid());
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationMessageConfig.getInvalidCredentials());
        }
    }


    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        if (authenticationService.verifyUser(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(authenticationMessageConfig.getEmailAlreadyVerified());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getLoggedInUserProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            authenticationService.forgotPassword(email);
            return ResponseEntity.status(HttpStatus.OK).body(authenticationMessageConfig.getForgotPasswordOk());
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authenticationMessageConfig.getEmailInvalid());
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(authenticationMessageConfig.getErrorSendingVerificationEmail());
        }
    }

    @Operation(summary = "method to reset password", method = "POST")
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetBody passwordResetBody) {
        authenticationService.resetPassword(passwordResetBody);
        return ResponseEntity.ok().build();
    }

}
