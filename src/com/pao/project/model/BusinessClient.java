package com.pao.project.model;

import java.util.List;

public class BusinessClient extends Client {
    private String companyName, CUI, contactPerson;
    private Double monthlyRevenue, monthlyExpenses;

    public BusinessClient(String id, String address, String email, String phone, String companyName, String CUI, String contactPerson, Double monthlyRevenue, Double monthlyExpenses) {
        super(id, address, email, phone);
        this.companyName = companyName;
        this.CUI = CUI;
        this.contactPerson = contactPerson;
        this.monthlyRevenue = monthlyRevenue;
        this.monthlyExpenses = monthlyExpenses;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCUI() {
        return CUI;
    }

    public Double getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public Double getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public ClientType getClientType(){
        return ClientType.BUSINESS;
    }
    public String getDisplayName(){
        return companyName;
    }
    public String getIdentificationNumber(){
        return CUI;
    }

}
