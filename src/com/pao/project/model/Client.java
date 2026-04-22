package com.pao.project.model;

import java.util.List;

public abstract class Client {

    private String id, address, email, phone;
    List<Account> conturi;
    protected abstract String getClientType();
    protected abstract String getDisplayname();
    protected abstract String getIdentificationNumber();
}
