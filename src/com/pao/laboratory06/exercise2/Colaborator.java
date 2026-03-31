package com.pao.laboratory06.exercise2;
import java.util.Scanner;
public abstract class Colaborator implements IOperatiiCitireScriere{
    protected String nume;
    protected String prenume;
    protected double venitLunar;
    protected abstract double calculeazaVenitAn();
    TipColaborator tip;

    Colaborator(String nume, String prenume, double venit, TipColaborator tip){
        this.nume= nume;
        this.prenume = prenume;
        this.venitLunar = venit;
        this.tip = tip;
    }
    public String getNume(){
        return nume;
    }
    public String getPrenume(){
        return prenume;
    }
    public double getVenitLunar(){
        return venitLunar;
    }
    public TipColaborator getTipColaborator(){
        return tip;
    }
//    @Override
//    public void citire(Scanner in){
//        this.nume = in.next();
//        this.prenume = in.next();
//        this.venitLunar = in.nextDouble();
//    }

}
