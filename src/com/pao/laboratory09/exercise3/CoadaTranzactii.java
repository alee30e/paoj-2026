package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.Tranzactie;

import java.util.LinkedList;
import java.util.Queue;

public class CoadaTranzactii {
    private final int capacitate = 5;
    private int folosit;
    Queue<Tranzactie> coada = new LinkedList<>();

    public synchronized void adauga(Tranzactie t) throws InterruptedException {
        while(coada.size() == capacitate) wait();
        coada.add(t);
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while(coada.isEmpty()) wait();
        Tranzactie t = coada.poll();
        notifyAll();
        return t;
    }

}
