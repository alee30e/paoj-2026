package com.pao.project.model;

public class IndividualClient extends Client {
    private String firstName, lastName, cnp, dateOfBirth, occupation;
    private Double monthlyIncome;

    public IndividualClient(String address, String email, String phone, String firstName, String lastName,
                            String cnp, String occupation, String dateOfBirth, Double monthlyIncome) {
        super(address, email, phone);
        this.firstName = firstName;
        this.lastName = lastName;
        this.cnp = cnp;
        this.occupation = occupation;
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

    public String getOccupation() {
        return occupation;
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
    @Override
    public String toString() {
        return "IndividualClient{" + "id='" + getId() + '\'' + ", name='" + getDisplayName() + '\'' +
                ", cnp='" + cnp + '\'' + ", dateOfBirth='" + dateOfBirth + '\'' + ", occupation='" + occupation + '\'' +
                ", monthlyIncome=" + monthlyIncome + ", email='" + getEmail() + '\'' + ", phone='" + getPhone() + '\'' +
                ", address='" + getAddress() + '\'' + ", numberOfAccounts=" + getAccounts().size() + '}';
    }
}
