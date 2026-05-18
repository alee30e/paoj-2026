package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<Tranzactie> tranzactii = List.of(
                new Tranzactie(1, new BigDecimal("1200.00"), LocalDate.of(2026, 5, 1), "RO", "WEB"),
                new Tranzactie(2, new BigDecimal("300.00"), LocalDate.of(2026, 5, 2), "RO", "ATM"),
                new Tranzactie(3, new BigDecimal("900.00"), LocalDate.of(2026, 5, 3), "DE", "APP"),
                new Tranzactie(4, new BigDecimal("1500.00"), LocalDate.of(2026, 5, 4), "FR", "WEB"),
                new Tranzactie(5, new BigDecimal("1500.00"), LocalDate.of(2026, 5, 5), "RO", "CRYPTO"),
                new Tranzactie(6, new BigDecimal("700.00"), LocalDate.of(2026, 5, 6), "DE", "POS"),
                new Tranzactie(7, new BigDecimal("2000.00"), LocalDate.of(2026, 5, 7), "RO", "APP"),
                new Tranzactie(8, new BigDecimal("100.00"), LocalDate.of(2026, 5, 8), "FR", "ATM")
        );

        Snapshot snapshot = tranzactii.stream().collect(CustomCollectors.toSnapshot(3));

        System.out.println("Top tranzactii");
        snapshot.getTopTranzactii().forEach(System.out::println);

        System.out.println();

        System.out.println("Numar tranzactii pe tara, descrescator");
        snapshot.getCountByCountry().entrySet().stream().sorted(Main::compareByValueDescThenKeyAsc)
                .forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue() + " tranzactii"));

        System.out.println();

        System.out.println("Numar tranzactii pe canal, descrescator");
        snapshot.getCountByChannel().entrySet().stream().sorted(Main::compareByValueDescThenKeyAsc)
                .forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue() + " tranzactii"));

        System.out.println();

        System.out.println("Suma totala");
        System.out.println("Total = " + snapshot.getTotalAmount());
    }

    private static int compareByValueDescThenKeyAsc(
            Map.Entry<String, Long> e1,
            Map.Entry<String, Long> e2
    ) {
        int cmp = Long.compare(e2.getValue(), e1.getValue());

        if (cmp != 0) {
            return cmp;
        }

        return e1.getKey().compareTo(e2.getKey());
    }
}