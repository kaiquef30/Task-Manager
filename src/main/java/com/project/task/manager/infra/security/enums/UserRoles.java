package com.project.task.manager.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRoles {

    ADMIN("ADMIN"),
    USER("USER"),
    MODERATOR("MODERATOR"),
    GUEST("GUEST"),

    ;

    private final String roles;

}

