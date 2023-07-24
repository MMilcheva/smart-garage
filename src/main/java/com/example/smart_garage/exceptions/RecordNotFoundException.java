package com.example.smart_garage.exceptions;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String message){
        super(message);
    }

}
