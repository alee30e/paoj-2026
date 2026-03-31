package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends PersoanaJuridica{
    double cheltuieliLunare;

    public SRLColaborator(){
        super();
    }

    public SRLColaborator(String nume, String prenume, double venit, TipColaborator tip, double cheltuieli){
        super(nume, prenume, venit, TipColaborator.SRL);
        this.cheltuieliLunare = cheltuieli;
    }

    public double calculeazaVenitAn(){
        return (getVenitLunar() - cheltuieliLunare) * 12 * 0.84;
    }
    public TipColaborator getTip(){
        return tip;
    }
    @Override
    public void citire(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
        this.tip = TipColaborator.SRL;
    }
    @Override
    public String tipContract() {
        return "SRL";
    }
}
