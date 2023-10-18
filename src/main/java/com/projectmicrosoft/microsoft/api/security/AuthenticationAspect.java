package com.projectmicrosoft.microsoft.api.security;



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
import java.util.Objects;

@Aspect
@Component
public class AuthenticationAspect {

    private static final String PERMISSION_DENIED = "You do not have permission to perform this action.";

    @Before("@annotation(authenticatedUser)")
    public void checkAuthentication(JoinPoint joinPoint, AuthenticatedUser authenticatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || !(authentication.getPrincipal() instanceof User)) {
            sendUnauthorizedResponse();
        }
        User user = (User) authentication.getPrincipal();
        String requiredRole = authenticatedUser.requiredRole();
        if (!userHasRequiredRole(user, requiredRole)) {
            sendUnauthorizedResponse();
        }
    }

    private void sendUnauthorizedResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (response != null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try {
                response.getWriter().write(PERMISSION_DENIED);
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean userHasRequiredRole(User user, String requiredRole) {
        return user.getRoles() != null && user.getRoles().getRole().equals(requiredRole);
    }


}
