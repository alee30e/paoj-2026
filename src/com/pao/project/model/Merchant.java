package com.pao.project.model;

public class Merchant {
    private static int nextId = 1;

    private String id;
    private String name;
    private MerchantCategory category;
    private BusinessClient businessClient;
    private Account settlementAccount;
    private Boolean active;

    public Merchant(String name, MerchantCategory category,
                    BusinessClient businessClient, Account settlementAccount) {
        this.id = generateId();
        this.name = name;
        this.category = category;
        this.businessClient = businessClient;
        this.settlementAccount = settlementAccount;
        this.active = true;
    }

    private static String generateId() {
        return "MER_" + nextId++;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MerchantCategory getCategory() {
        return category;
    }

    public BusinessClient getBusinessClient() {
        return businessClient;
    }

    public Account getSettlementAccount() {
        return settlementAccount;
    }

    public Boolean getActive() {
        return active;
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Merchant{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", category=" + category + ", businessClient="
                + businessClient.getDisplayName() + ", settlementAccount=" + settlementAccount.getIBAN() + ", active=" + active + '}';
    }
}