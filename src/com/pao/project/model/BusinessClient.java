package com.pao.project.model;

public class BusinessClient extends Client {
    private String companyName, CUI, contactPerson;
    private Double monthlyRevenue, monthlyExpenses;

    protected ClientType getClientType(){
        return ClientType.BUSINESS;
    }
    protected String getDisplayName(){
        return companyName;
    }
    protected String getIdentificationNumber(){
        return CUI;
    }

}
