package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati;

    private AngajatService() {
        this.angajati = new Angajat[0];
    }
    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }
    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }
    public void addAngajat(Angajat a) {
        Angajat[] temp = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, temp, 0, angajati.length);
        temp[angajati.length] = a;
        angajati = temp;
        System.out.println("Angajat:" + a);
    }
    public void printAll() {
        for (Angajat a : angajati) {
            System.out.println(a);
        }
    }
    public void listBySalary() {
        Angajat[] temp = angajati.clone();
        Arrays.sort(temp);
        for (Angajat a : temp) {
            System.out.println(a);
        }
    }
    public void findByDepartament(String numeDept) {
        boolean gasit = false;
        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(a);
                gasit = true;
            }
        }
        if (!gasit) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }
}
