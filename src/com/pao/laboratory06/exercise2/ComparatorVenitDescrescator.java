package com.pao.laboratory06.exercise2;

import java.util.Comparator;

public class ComparatorVenitDescrescator implements Comparator<Colaborator> {
    public int compare(Colaborator a, Colaborator b){
        return Double.compare(b.calculeazaVenitAn(), a.calculeazaVenitAn());
    }
}
