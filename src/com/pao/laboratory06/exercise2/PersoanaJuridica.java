package com.pao.laboratory06.exercise2;

public abstract class PersoanaJuridica extends Colaborator{
    TipColaborator tip;
    public PersoanaJuridica(){
        super();
    }
    public PersoanaJuridica(String nume, String prenume, double venit, TipColaborator tip){
        super(nume, prenume, venit, tip);
    }
    public abstract double calculeazaVenitAn();
}
