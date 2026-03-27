package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati = new Angajat[0];
    private AngajatService() {
    }
    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }
    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }
    public void addAngajat(Angajat a) {
        Angajat[] nou = Arrays.copyOf(angajati, angajati.length + 1);
        nou[angajati.length] = a;
        angajati = nou;
        System.out.println("Angajat: " + a.getNume());
    }
    public void printAll() {
        if (angajati.length == 0) {
            System.out.println("Nu exista");
            return;
        }
        System.out.println("Lista angajati");
        for (int i = 0; i < angajati.length; i++) {
            System.out.println((i + 1) + ". " + angajati[i]);
        }
    }
    public void listBySalary() {
        if (angajati.length == 0) {
            System.out.println("Nu exista");
            return;
        }

        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);

        System.out.println("Salariu - descrescator");
        for (int i = 0; i < copy.length; i++) {
            System.out.println((i + 1) + ". " + copy[i]);
        }
    }

    public void findByDepartament(String numeDept) {
        boolean gasit = false;

        System.out.println("Departament " + numeDept);
        for (Angajat angajat : angajati) {
            if (angajat.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(angajat);
                gasit = true;
            }
        }

        if (!gasit) {
            System.out.println("Nu exista ang in " + numeDept);
        }
    }
}