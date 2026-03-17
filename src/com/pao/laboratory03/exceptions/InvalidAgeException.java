package com.pao.laboratory03.exceptions;

public class InvalidAgeException extends RuntimeException{
//    private final int age;
    public InvalidAgeException(String message) {
        super(message);
//        this.age = age;
    }
}
