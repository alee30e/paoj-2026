package com.pao.laboratory07.exercise3;

public non-sealed class ComandaGratuita extends Comanda{
//    private double pret;

    public ComandaGratuita(String nume, String client) {
        super(nume, client);
    }
    public double pretFinal(){
        return 0.0;
    }
    public String descriere(){
        return String.format("GIFT: %s, gratuit [%s]", nume, orderState);
    }
}
