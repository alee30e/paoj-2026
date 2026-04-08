package com.pao.laboratory06.exercise3;

public abstract class Persoana {
    private String nume, prenume, telefon;
    public Persoana(){}
    public Persoana(String nume, String prenume, String telefon){
        this.nume = nume;
        this.prenume = prenume;
        this.telefon = telefon;
    }
    public String getNume(){
        return this.nume;
    }
    public String getPrenume(){
        return this.prenume;
    }
    public String getTelefon(){
        return this.telefon;
    }
    @Override
    public String toString(){
        return "";
    }

}
