package com.pao.project.model;

public class IndividualClient extends Client {
    private String firstName, lastName, cnp, dateOfBirth, ocuppation;
    private Double monthlyIncome;

    public IndividualClient(String id, String address, String email, String phone, String firstName, String lastName,
                            String cnp, String ocuppation, String dateOfBirth, Double monthlyIncome) {
        super(id, address, email, phone);
        this.firstName = firstName;
        this.lastName = lastName;
        this.cnp = cnp;
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
        return cnp;
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

//    public validateBirth

    public ClientType getClientType(){
        return ClientType.INDIVIDUAL;
    }
    public String getDisplayName(){
        return firstName + " " + lastName;
    }
    public String getIdentificationNumber(){
        return cnp;
    }
}
