package com.example.smart_garage.exceptions;

public class DuplicateUsernameException extends  RuntimeException{

    public DuplicateUsernameException(String type, String attribute, String value) {
        super(String.format("%s with %s %s already exists.", type, attribute, value));
    }
}
