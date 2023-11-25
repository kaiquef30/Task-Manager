package com.projectmicrosoft.microsoft.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private  HttpStatus status;

    private  String message;

    private  String details;

}
