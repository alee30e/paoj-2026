package com.pao.laboratory06.exercise2;

public abstract class PersoanaFizica extends Colaborator{
    TipColaborator tip;
    public PersoanaFizica(){
        super();
    }
    public PersoanaFizica(String nume, String prenume, double venit, TipColaborator tip){
        super(nume, prenume, venit, tip);
    }
    public abstract double calculeazaVenitAn();
}
