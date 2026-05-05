package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.TipTranzactie;
import com.pao.laboratory09.exercise1.Tranzactie;

public class ATMThread extends Thread{
    private final int atmId;
    private CoadaTranzactii coada;

    public ATMThread(int atmId, CoadaTranzactii coada){
        this.atmId = atmId;
        this.coada = coada;
    }

    @Override
    public void run(){
        for (int i = 0; i< 4; i++){
            int tranzactieId = (atmId - 1) * 4 + i;
            double suma = 100 * tranzactieId;

            Tranzactie t = new Tranzactie(tranzactieId, suma, "2026-05-08", "", "", TipTranzactie.CREDIT);

            System.out.printf("[ATM-%d] trimite: Tranzactie #%d %.2f RON%n", atmId, tranzactieId, suma);

            try {
                coada.adauga(t);
                Thread.sleep(50);

            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }
}
