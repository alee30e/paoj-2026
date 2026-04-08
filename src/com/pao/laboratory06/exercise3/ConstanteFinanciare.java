package com.pao.laboratory06.exercise3;

public enum ConstanteFinanciare {
    TVA(0.2), SALARIU_MINIM(4325), COTA_IMPOZIT(0.16);
    private final double valoare;
    ConstanteFinanciare(double val){
        this.valoare = val;
    }
    public double getVal(){
        return valoare;
    }
}
