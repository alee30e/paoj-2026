package com.pao.laboratory05.angajati;

import java.util.Scanner;

/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();
        while (true) {
            System.out.println("Meniu");
            System.out.println("1.Adauga");
            System.out.println("2.Dupa salariu");
            System.out.println("3.Dupa departament");
            System.out.println("0.Gata");
            System.out.print("Optiune: ");
            String optiune = scanner.nextLine();

            switch (optiune) {
                case "1" -> {
                    System.out.print("Nume: ");
                    String nume = scanner.nextLine();

                    System.out.print("Departament: ");
                    String numeDepartament = scanner.nextLine();

                    System.out.print("Locatie departament: ");
                    String locatieDepartament = scanner.nextLine();

                    System.out.print("Salariu: ");
                    double salariu = Double.parseDouble(scanner.nextLine());
                    Departament departament = new Departament(numeDepartament, locatieDepartament);
                    Angajat angajat = new Angajat(nume, departament, salariu);
                    service.addAngajat(angajat);
                }
                case "2" -> service.listBySalary();
                case "3" -> {
                    System.out.print("Departament: ");
                    String numeDept = scanner.nextLine();
                    service.findByDepartament(numeDept);
                }
                case "0" -> {
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid");
            }
        }
    }
}
