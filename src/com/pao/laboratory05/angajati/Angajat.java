package com.pao.laboratory05.angajati;

public class Angajat implements Comparable<Angajat>{
    private String nume;
    private Departament departament;
    private double salariu;
    public Angajat(String nume, Departament d, double sal){
        this.nume = nume;
        this.departament = d;
        this.salariu = sal;
    }
    public String getNume() {
        return nume;
    }
    public Departament getDepartament() {
        return departament;
    }
    public double getSalariu() {
        return salariu;
    }
    @Override
    public int compareTo(Angajat other) {
        return Double.compare(other.salariu, this.salariu);
    }
    @Override
    public String toString() {
        return "Angajat{nume='" + nume + "', departament=" + departament + ", salariu=" + salariu + "}";
    }
}
