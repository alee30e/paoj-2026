package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica{
    boolean bonus;
    public CIMColaborator(String nume, String prenume, double venit, TipColaborator tip, boolean bonus){
        super(nume, prenume, venit, TipColaborator.CIM);
        this.bonus = bonus;
    }
    public TipColaborator getTip(){
        return tip;
    }
    public CIMColaborator(){
        super();
    }
    public CIMColaborator(String nume, String prenume, double venit, TipColaborator tip){
        super(nume, prenume, venit, TipColaborator.CIM);
        this.bonus = false;
    }
    @Override
    public double calculeazaVenitAn(){
        int b = 0;
        if (bonus) b = 1;
        return 12 * getVenitLunar() * 0.55 + 0.1 * b * (12 * getVenitLunar() * 0.55);
    }
    @Override
    public void citire(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitLunar = in.nextDouble();

        if (in.hasNext()) {
            String posibilBonus = in.next();
            this.bonus = posibilBonus.equalsIgnoreCase("DA");
        } else {
            this.bonus = false;
        }

        this.tip = TipColaborator.CIM;
    }

    @Override
    public String tipContract() {
        return "CIM";
    }
    @Override
    public boolean areBonus() {
        return bonus;
    }
}
