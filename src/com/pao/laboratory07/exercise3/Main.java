package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise1.OrderCommand;
import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;
import com.pao.laboratory07.exercise3.Comanda;
import com.pao.laboratory07.exercise3.ComandaGratuita;
import com.pao.laboratory07.exercise3.ComandaRedusa;
import com.pao.laboratory07.exercise3.ComandaStandard;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();
        int nrStandard = 0, nrDiscounted = 0, nrGift = 0;
        double sumaStandard = 0, sumaDiscounted = 0;
        for (int i = 0; i < n; i++) {
            String tip = sc.next();

            if (tip.equals("STANDARD")) {
                String nume = sc.next();
                double pret = sc.nextDouble();
                String client = sc.next();
                comenzi.add(new ComandaStandard(nume, pret, client));
            } else if (tip.equals("DISCOUNTED")) {
                String nume = sc.next();
                double pret = sc.nextDouble();
                int discount = sc.nextInt();
                String client = sc.next();
                comenzi.add(new ComandaRedusa(nume, pret, discount, client));
            } else if (tip.equals("GIFT")) {
                String nume = sc.next();
                String client = sc.next();
                comenzi.add(new ComandaGratuita(nume, client));
            }
        }
//        for (Comanda c : comenzi) {
//            System.out.println(c.descriere());
//        }
        System.out.println();

        while (sc.hasNext()) {
            OptiuniMeniu optiuneMeniu = OptiuniMeniu.valueOf(sc.next());
            System.out.println("AM CITIT: [" + optiuneMeniu + "]");
            switch (optiuneMeniu) {
                case STATS -> {
                    System.out.println("STATS\n");
                    Map<String, Double> medii = comenzi.stream().collect(Collectors.groupingBy(Comanda::getTip,
                                    Collectors.averagingDouble(Comanda::pretFinal)));

                    if (medii.containsKey("STANDARD")) {
                        System.out.printf("STANDARD: medie = %.2f lei", medii.get("STANDARD"));
                        System.out.println();
                    }
                    if (medii.containsKey("DISCOUNTED")) {
                        System.out.printf("DISCOUNTED: medie = %.2f lei", medii.get("DISCOUNTED"));
                        System.out.println();
                    }
                    if (medii.containsKey("GIFT")) {
                        System.out.printf("GIFT: medie = %.2f lei", medii.get("GIFT"));
                        System.out.println();
                    }
                    System.out.println();
                }
                case FILTER -> {
                    double prag = sc.nextDouble();
                    System.out.println("FILTER " + prag + "\n");
//                    for (Comanda c : comenzi){
//                        if (c.pretFinal() >= prag)
//                            System.out.println(c.descriereClient());
//                    }
                    List<Comanda> filtrate = comenzi.stream().filter(c -> c.pretFinal() >= prag).toList();
                    for (Comanda c : filtrate) {
                        System.out.println(c.descriereClient());
                    }
                    System.out.println();
                }
                case SORT -> {
                    System.out.println("SORT\n");
//                    List<Comanda> comenziSortate = new ArrayList<>(comenzi);
//                    comenziSortate.sort(new ComandaComparator());
//                    for (Comanda c : comenziSortate){
//                        System.out.println(c.descriereClient());
//                    }

                    List<Comanda> comenziSortate = comenzi.stream().sorted(Comparator.comparing(Comanda::getClient)
                                    .thenComparing(Comanda::pretFinal)).toList();

                    for (Comanda c : comenziSortate) {
                        System.out.println(c.descriereClient());
                    }
                    System.out.println();
                }
                case SPECIAL -> {
                    System.out.println("SPECIAL\n");
                    List<Comanda> comenziSpeciale = comenzi.stream()
                            .filter(c -> c instanceof ComandaRedusa cr && cr.getDiscountProcent() > 15).toList();

                    for (Comanda c : comenziSpeciale) {
                        System.out.println(c.descriereClient());
                    }
                    System.out.println();
                }
                case QUIT -> {
                    System.out.println("QUIT");
                    return;
                }
            }
        }
    }
}
