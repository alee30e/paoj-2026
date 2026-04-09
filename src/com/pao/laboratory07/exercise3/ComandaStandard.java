package com.pao.laboratory07.exercise3;

public non-sealed class ComandaStandard extends Comanda{
    private double pret;

    public ComandaStandard(String nume, double pret, String client) {
        super(nume, client);
        this.pret = pret;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public double getPret() {
        return pret;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }
    public double pretFinal(){
        return pret;
    }
    public String descriere(){
        return String.format("STANDARD: %s, pret: %.2f lei [%s]", nume, pret, orderState);
    }
}
