package com.pao.laboratory06.exercise3;

public interface PlataOnline {
//    boolean trimiteSMS(String message);
    void autentificare(String user, String parola);
    double consultareSold();
    boolean efectuarePlata(double suma);
}
