package com.projectmicrosoft.microsoft.enums;

public enum UserRoles {

    ADMIN("ADMIN"),
    USER("USER"),
    MODERATOR("MODERATOR"),
    GUEST("GUEST"),

    ;

    private final String roles;

    UserRoles(String role) {
        this.roles = role;
    }

    public String getRole() {
        return roles;
    }
}

