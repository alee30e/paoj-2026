package com.pao.project.model;

public class InvidualClient {
    private String firstName, lastName, CNP, dateOfBirth, ocuppation;
    private Double monthlyIncome;

    protected ClientType getClientType(){
        return ClientType.INVIDUAL;
    }
    protected String getDisplayName(){
        return firstName + " " + lastName;
    }
    protected String getIdentificationNumber(){
        return CNP;
    }
}
