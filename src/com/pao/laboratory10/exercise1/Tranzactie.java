package com.pao.laboratory10.exercise1;

public class Tranzactie {
    int id;
    double suma;
    String data;
    TipTranzactie tip;

    public  Tranzactie(int id, double suma, String data, TipTranzactie tip){
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.tip = TipTranzactie.valueOf(String.valueOf(tip));
    }

    public int getId() {
        return id;
    }

    public double getSuma() {
        return suma;
    }

    public String getData() {
        return data;
    }

    public TipTranzactie getTip() {
        return tip;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + data + " " + tip + ": " + String.format("%.2f", suma) + " RON";
    }
}
