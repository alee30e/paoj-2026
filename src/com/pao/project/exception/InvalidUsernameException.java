package com.pao.project.exception;

public class InvalidUsernameException extends RuntimeException{
    public InvalidUsernameException(String message){
        super(message);
    }
}
