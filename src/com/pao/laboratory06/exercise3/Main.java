package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Inginer[] ingineri = {
                new Inginer("Popescu", "Ana", "0712345678", 9000, 2500, "ana.popescu", "pass1"),
                new Inginer("Ionescu", "Mihai", "0723456789", 12000, 4000, "mihai.ionescu", "pass2"),
                new Inginer("Georgescu", "Radu", "0734567890", 10000, 1500, "radu.georgescu", "pass3")
        };

        System.out.println("Ingineri nesortati");
        for (Inginer ing : ingineri) {
            System.out.println(ing.getNume() + ", salariu = " + ing.getSalariu());
        }

        Arrays.sort(ingineri);
        System.out.println("Ingineri sortati dupa nume");
        for (Inginer ing : ingineri) {
            System.out.println(ing.getNume() + ", salariu = " + ing.getSalariu());
        }

        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        System.out.println("Ingineri - descrescator");
        for (Inginer ing : ingineri) {
            System.out.println(ing.getNume() + ", salariu = " + ing.getSalariu());
        }

        PlataOnline contInginer = new Inginer(
                "Marin", "Teodor", "0745678901", 11000, 3000, "teodor.marin", "abc123"
        );

        contInginer.autentificare("teodor.marin", "abc123");
        System.out.println("sold curent: " + contInginer.consultareSold());
        System.out.println("plata 500: " + contInginer.efectuarePlata(500));
        System.out.println("sold dupa plata: " + contInginer.consultareSold());

        //nu merge
        //contInginer.getSalariu();
        //contInginer.trimiteSMS("Salut");

        PlataOnlineSMS firma1 = new PersoanaJuridica(
                "SC", "AlfaSRL", "0711111111", "alfa_user", "firma123", 10000
        );

        firma1.autentificare("alfa_user", "firma123");
        System.out.println("sold firma: " + firma1.consultareSold());
        System.out.println("plata 1200: " + firma1.efectuarePlata(1200));
        System.out.println("SMS valid: " + firma1.trimiteSMS("plata procesata"));

        PersoanaJuridica pj1 = (PersoanaJuridica) firma1;
        System.out.println("mesaje trimise firmei 1: " + pj1.getSmsTrimise());

        // 4. Caz fără telefon valid
        System.out.println("fara telefon valid");
        PlataOnlineSMS firma2 = new PersoanaJuridica(
                "SC", "BetaSRL", "", "beta_user", "beta123", 8000
        );

        System.out.println("SMS catre firma - telefon invalid: " +
                firma2.trimiteSMS("mesaj test"));

        PersoanaJuridica pj2 = (PersoanaJuridica) firma2;
        System.out.println("mesaje trimise firmei 2: " + pj2.getSmsTrimise());

        System.out.println("mesaj invalid");
        System.out.println("SMS null: " + firma1.trimiteSMS(null));
        System.out.println("SMS gol: " + firma1.trimiteSMS(""));

        System.out.println("constante");
        System.out.println("TVA = " + ConstanteFinanciare.TVA.getVal());
        System.out.println("salariu minim = " + ConstanteFinanciare.SALARIU_MINIM.getVal());

        System.out.println("erori");
        try {
            contInginer.autentificare(null, "abc123");
        } catch (IllegalArgumentException e) {
            System.out.println("eroare: " + e.getMessage());
        }
    }
}