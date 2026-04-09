package com.pao.laboratory07.exercise3;

public non-sealed class ComandaRedusa extends Comanda{
    private double pret;
    private int discountProcent;

    public ComandaRedusa(String nume, double pret, int discount, String client) {
        super(nume, client);
        this.discountProcent = discount;
        this.pret = pret;
    }
    public double pretFinal(){
//        pret = pret * (1 - pret * discountProcent / 100.0);
        return pret * (1 - discountProcent / 100.0);
    }
    public String descriere(){
        return String.format("DISCOUNTED: %s, pret: %.2f lei (-%d%%) [%s]", nume, pretFinal(), discountProcent, orderState);
    }
}
