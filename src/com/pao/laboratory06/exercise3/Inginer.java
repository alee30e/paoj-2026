package com.pao.laboratory06.exercise3;

import static java.lang.CharSequence.compare;

public class Inginer extends Angajat implements Comparable<Inginer>, PlataOnline{
    private double sold;
    private String user;
    private String parola;
    public Inginer(){
        super();
    }

    public Inginer(String nume, String prenume, String telefon, double salariu, double sold, String user, String parola){
        super(nume, prenume, telefon, salariu);
        this.sold = sold;
        this.user = user;
        this.parola = parola;
    }
    public String getUser(){
        return user;
    }
    public String getParola(){
        return parola;
    }
    public double getSold(){
        return sold;
    }
    @Override
    public int compareTo(Inginer other){
        return compare(this.getNume(), other.getNume());
    }
    @Override
    public void autentificare(String user, String parola){
        if (user == null || parola == null || user.length() == 0 || parola.length() == 0)
            throw new java.lang.IllegalArgumentException("Campurile parola sau utilizator nu pot fi nule sau goale");
        if (!user.equals(getUser()) || !parola.equals(getParola()))
            throw new IllegalArgumentException("Parola sau utilizator gresite");
        System.out.println("Conectare reusita! User " + getUser());
    }
    @Override
    public double consultareSold(){
        return getSold();
    }
    @Override
    public boolean efectuarePlata(double suma){
        if (suma < 0)
            throw new IllegalArgumentException("Suma nu poate fi < 0");
        if (getSold() >= suma){
            sold = getSold() - suma;
            System.out.println("Plata efectuata cu succes! Sold dupa plata:" + getSold());
            return true;
        }
        System.out.println("Sold insuficient: " + getSold());
        return false;
    }

}
