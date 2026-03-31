package com.pao.laboratory06.exercise2;

public class CIMColaborator extends PersoanaFizica{
    boolean bonus;
    CIMColaborator(String nume, String prenume, double venit, TipColaborator tip, boolean bonus){
        super(nume, prenume, venit, TipColaborator.CIM);
        this.bonus = bonus;
    }
    CIMColaborator(String nume, String prenume, double venit, TipColaborator tip){
        super(nume, prenume, venit, TipColaborator.CIM);
        this.bonus = false;
    }
    public double calculeazaVenitAn(){
        int b = 0;
        if (bonus) b = 1;
        return 12 * getVenitLunar() * 0.55 + 0.1 * b * (12 * getVenitLunar() * 0.55);
    }
}
