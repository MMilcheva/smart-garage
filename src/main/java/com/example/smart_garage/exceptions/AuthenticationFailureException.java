package com.example.smart_garage.exceptions;

public class AuthenticationFailureException extends RuntimeException {

    public AuthenticationFailureException(String message){
        super(message);
    }
}
