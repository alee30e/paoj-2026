package com.pao.project.model;

import java.util.List;

public class InvidualClient extends Client {
    private String firstName, lastName, CNP, dateOfBirth, ocuppation;
    private Double monthlyIncome;

    public InvidualClient(String id, String address, String email, String phone, String firstName, String lastName, String CNP, String ocuppation, String dateOfBirth, Double monthlyIncome) {
        super(id, address, email, phone);
        this.firstName = firstName;
        this.lastName = lastName;
        this.CNP = CNP;
        this.ocuppation = ocuppation;
        this.dateOfBirth = dateOfBirth;
        this.monthlyIncome = monthlyIncome;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCNP() {
        return CNP;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getOcuppation() {
        return ocuppation;
    }

    public Double getMonthlyIncome() {
        return monthlyIncome;
    }

    public ClientType getClientType(){
        return ClientType.INVIDUAL;
    }
    public String getDisplayName(){
        return firstName + " " + lastName;
    }
    public String getIdentificationNumber(){
        return CNP;
    }
}
