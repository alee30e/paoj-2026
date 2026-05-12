package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise1.Tranzactie;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        String linie;
        List<Tranzactie> tranzactii = new ArrayList<>();

        tranzactii.add(new Tranzactie(1, 1500.00, "2024-01-15", TipTranzactie.CREDIT, "RO49BANK0001"));
        tranzactii.add(new Tranzactie(2, 750.50, "2024-01-18", TipTranzactie.DEBIT, "RO49BANK0002"));
        tranzactii.add(new Tranzactie(3, 3200.00, "2024-01-25", TipTranzactie.CREDIT, "RO49BANK0001"));
        tranzactii.add(new Tranzactie(4, 450.00, "2024-02-03", TipTranzactie.DEBIT, "RO49BANK0003"));
        tranzactii.add(new Tranzactie(5, 980.75, "2024-02-10", TipTranzactie.CREDIT, "RO49BANK0004"));
        tranzactii.add(new Tranzactie(6, 1200.00, "2024-02-16", TipTranzactie.DEBIT, "RO49BANK0002"));
        tranzactii.add(new Tranzactie(7, 4100.00, "2024-03-02", TipTranzactie.CREDIT, "RO49BANK0005"));
        tranzactii.add(new Tranzactie(8, 640.40, "2024-03-08", TipTranzactie.DEBIT, "RO49BANK0001"));
        tranzactii.add(new Tranzactie(9, 2750.00, "2024-03-14", TipTranzactie.CREDIT, "RO49BANK0003"));
        tranzactii.add(new Tranzactie(10, 300.00, "2024-03-20", TipTranzactie.DEBIT, "RO49BANK0004"));
        tranzactii.add(new Tranzactie(11, 1800.00, "2024-04-05", TipTranzactie.CREDIT, "RO49BANK0002"));
        tranzactii.add(new Tranzactie(12, 950.90, "2024-04-11", TipTranzactie.DEBIT, "RO49BANK0005"));

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()){
            linie = sc.nextLine();
            if (linie.startsWith("FILTER")){
                String[] cuv;
                cuv = linie.split(" ");
                TipTranzactie tip = TipTranzactie.valueOf(cuv[1]);
                tranzactii.stream().filter(t -> t.getTip() == tip).forEach(System.out::println);
            }
            else if (linie.equals("SUMA")){
                double total = tranzactii.stream().mapToDouble(Tranzactie::getSuma).sum();
                System.out.println("Suma totala: "+ total);
            }
            else if (linie.equals("PER_LUNA")) {
                Map<String, Double> raport = tranzactii.stream().collect(Collectors.groupingBy(t -> t.getData().substring(0, 7),
                        Collectors.summingDouble(Tranzactie::getSuma)));
                for (Map.Entry<String, Double> ent : raport.entrySet()) {
                    System.out.println(ent.getKey() + " " + ent.getValue() + " RON");
                }
            }
            else if (linie.equals("TOP3")){
                tranzactii.stream().sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed()).limit(3).forEach(System.out::println);
            }
            else if (linie.equals("MEDIE")){
                OptionalDouble medie = tranzactii.stream().mapToDouble(Tranzactie::getSuma).average();
                System.out.println("Suma medie: "+ medie + " RON");
            }
            else if (linie.equals("UNICE")){
                List<String> listaUnice = tranzactii.stream().map(Tranzactie::getContSursa).distinct().collect(toList());
                System.out.println("Conturi sursa unice: "+ listaUnice);
            }
            else if (linie.equals("EXTRAS")){
                Map<String, List<Tranzactie>> lista = tranzactii.stream().collect(Collectors.groupingBy(t -> t.getData().substring(0, 7),
                        TreeMap::new, Collectors.toList()));

                for (Map.Entry<String, List<Tranzactie>> ent : lista.entrySet()){
                    String data = ent.getKey();
                    List<Tranzactie> tranz = new ArrayList<>(ent.getValue());
                    double total;
                    int nrTranzactii = tranz.size();

                    total = tranz.stream().mapToDouble(Tranzactie::getSuma).sum();

                    System.out.println("Extras de cont " + data + ": "+ nrTranzactii + ", total: "+ total);
                }
            }

        }
    }
}
