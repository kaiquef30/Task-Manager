package com.projectmicrosoft.microsoft.api.security;


import com.projectmicrosoft.microsoft.enums.UserRoles;
import com.projectmicrosoft.microsoft.model.User;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

@Aspect
@Component
public class AuthenticationAspect {

    private static final String PERMISSION_DENIED = "You do not have permission to perform this action.";

    @Before("@annotation(authenticatedUser)")
    public void checkAuthentication(JoinPoint joinPoint, AuthenticatedUser authenticatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || !(authentication.getPrincipal() instanceof User user)) {
            sendUnauthorizedResponse();
            return;
        }
        String[] requiredRoles = authenticatedUser.requiredRoles();

        if (!userHasRequiredRoles(user, requiredRoles)) {
            sendUnauthorizedResponse();
        }
    }

    private void sendUnauthorizedResponse() {
        HttpServletResponse response = ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        if (response != null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try (OutputStream outputStream = response.getOutputStream()) {
                outputStream.write(PERMISSION_DENIED.getBytes());
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean userHasRequiredRoles(User user, String[] requiredRoles) {
        if (requiredRoles.length == 0) {
            return true;
        }

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return false;
        }

        for (String requiredRole : requiredRoles) {
            for (UserRoles userRole : user.getRoles()) {
                if (userRole.name().equals(requiredRole)) {
                    return true;
                }
            }
        }
        return false;
    }
}