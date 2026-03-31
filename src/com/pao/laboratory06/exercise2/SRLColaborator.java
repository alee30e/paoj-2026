package com.pao.laboratory06.exercise2;

public class SRLColaborator extends PersoanaJuridica{
    double cheltuieliLunare;

    SRLColaborator(String nume, String prenume, double venit, TipColaborator tip, double cheltuieli){
        super(nume, prenume, venit, TipColaborator.SRL);
        this.cheltuieliLunare = cheltuieli;
    }

    public double calculeazaVenitAn(){
        return (getVenitLunar() - cheltuieliLunare) * 12 * 0.84;
    }
}
