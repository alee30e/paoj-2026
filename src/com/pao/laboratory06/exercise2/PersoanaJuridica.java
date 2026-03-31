package com.pao.laboratory06.exercise2;

public abstract class PersoanaJuridica extends Colaborator{
    TipColaborator tip;
    PersoanaJuridica(String nume, String prenume, double venit, TipColaborator tip){
        super(nume, prenume, venit, tip);
    }
}
