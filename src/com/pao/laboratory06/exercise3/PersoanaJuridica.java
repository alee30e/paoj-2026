package com.pao.laboratory06.exercise3;

import java.util.*;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS{
    private List<String> smsTrimise;
    private String user;
    private String parola;
    private double sold;
    public PersoanaJuridica(String nume, String prenume, String telefon, String user, String parola, double sold){
        super(nume, prenume, telefon);
        this.smsTrimise = new ArrayList<>();
        this.user = user;
        this.parola = parola;
        this.sold = sold;
    }
    @Override
    public boolean trimiteSMS(String mesaj){
        if (mesaj == null || mesaj.equals(""))
            return false;
        if (this.getTelefon() == null || !this.getTelefon().matches("^07\\d{8}$")){
            return false;
        }
        smsTrimise.add(mesaj);
        return true;
    }
    @Override
    public void autentificare(String user, String parola){
        if (user == null || parola == null || user.length() == 0 || parola.length() == 0)
            throw new IllegalArgumentException("Campurile parola sau utilizator nu pot fi nule sau goale");
        if (!user.equals(user) && !parola.equals(parola))
            throw new IllegalArgumentException("Parola sau utilizator gresite");
        System.out.println("Conectare reusita! Firma: " + user);
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
    public double consultareSold(){
        return getSold();
    }
    public void setUser(String user){
        this.user = user;
    }
    public void setParola(String parola){
        this.parola = parola;
    }
    public void setSold(Double sold){
        this.sold = sold;
    }
    public List<String> getSmsTrimise(){
        return new ArrayList<>(smsTrimise);
    }

    @Override
    public boolean efectuarePlata(double suma){
        if (suma < 0)
            throw new IllegalArgumentException("Suma nu poate fi < 0");
        if (sold >= suma){
            sold = sold - suma;
            System.out.println("Plata efectuata cu succes! Sold dupa plata:" + sold);
            return true;
        }
        System.out.println("Sold insuficient: " + sold);
        return false;
    }
}
