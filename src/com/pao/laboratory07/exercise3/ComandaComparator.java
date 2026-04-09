package com.pao.laboratory07.exercise3;

import java.util.Comparator;

public class ComandaComparator implements Comparator<Comanda> {
    @Override
    public int compare(Comanda a, Comanda b){
        int comp = a.getClient().compareTo(b.getClient());
        if (comp !=0) return comp;
        return Double.compare(a.pretFinal(), b.pretFinal());
    }
}
