package com.pao.project.model;

import java.util.List;

public abstract class Client {

    private String id, address, email, phone;
    List<Account> conturi;
    protected abstract ClientType getClientType();
    protected abstract String getDisplayName();
    protected abstract String getIdentificationNumber();
}
