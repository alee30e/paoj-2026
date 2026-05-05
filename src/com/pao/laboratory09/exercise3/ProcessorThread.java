package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.TipTranzactie;
import com.pao.laboratory09.exercise1.Tranzactie;

public class ProcessorThread implements Runnable{
    volatile boolean activ = true;
    private CoadaTranzactii coada;

    public ProcessorThread(CoadaTranzactii coada){
        this.coada = coada;
//        this.activ = true;
    }

    @Override
    public void run() {
        try {
            while (activ || !coada.coada.isEmpty()) {
                Tranzactie t = coada.extrage();
                Thread.sleep(80);
                System.out.printf("[PROCESOR] factura #%d: %.2f RON | %s%n", t.getId(), t.getSuma(), t.getData());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
