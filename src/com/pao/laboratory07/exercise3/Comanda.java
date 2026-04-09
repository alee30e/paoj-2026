package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected OrderState orderState;

    public String getClient() {
        return client;
    }

    protected String client;

    public Comanda(String nume, String client) {
        this.nume = nume;
        this.client = client;
        this.orderState = OrderState.PLACED;
    }
    public abstract double pretFinal();
    public abstract String descriere();
    public abstract String descriereClient();
    public abstract String getTip();
}