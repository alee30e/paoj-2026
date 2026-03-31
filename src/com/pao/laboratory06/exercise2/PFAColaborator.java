package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica{
    double cheltuieliLunare;
    double salMinBrut = 48600;

    public PFAColaborator(){
        super();
    }

    public PFAColaborator(String nume, String prenume, double venit, TipColaborator tip, double cheltuieliLunare){
        super(nume, prenume, venit, TipColaborator.PFA);
        this.cheltuieliLunare = cheltuieliLunare;
    }
    public TipColaborator getTip(){
        return tip;
    }
    public double calculeazaVenitAn(){
        double venitNet = (getVenitLunar() - cheltuieliLunare) * 12;
        double impozitVenit = 0.1 * venitNet;
        double cass, cas;
        if (venitNet < 6 * salMinBrut){
            cass = 0.1 * (6 * salMinBrut);
        }
        else if (venitNet <= 72 * salMinBrut){
            cass = 0.1 * venitNet;
        }
        else cass = 0.1 * (72 * salMinBrut);
        if (venitNet < 12 * salMinBrut) cas = 0;
        else if (venitNet <= 24 * salMinBrut) cas = 0.25 * (12 * salMinBrut);
        else cas = 0.25 * (24 * salMinBrut);
        double venitNetAnual = venitNet - impozitVenit - cass - cas;
        return venitNetAnual;
    }
    @Override
    public void citire(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
        this.tip = TipColaborator.PFA;
    }
    @Override
    public String tipContract() {
        return "PFA";
    }
}
