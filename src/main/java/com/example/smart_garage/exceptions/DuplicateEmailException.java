package com.example.smart_garage.exceptions;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String type, String attribute, String value) {
        super(String.format("%s with %s %s already exists.", type, attribute, value));
    }
}
