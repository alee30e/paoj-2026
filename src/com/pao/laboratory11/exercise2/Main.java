package com.pao.laboratory11.exercise2;

import com.pao.laboratory11.exercise1.ChannelScore;
import com.pao.laboratory11.exercise1.Tranzactie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        List<Tranzactie> tranzactii = new ArrayList<>();

        int n = Integer.parseInt(readNonEmptyLine(sc));
        
        for (int i = 0; i < n; i++) {
            String line = readNonEmptyLine(sc);
            String[] parts = line.trim().split("\\s+");

            int id = Integer.parseInt(parts[0]);
            double amount = Double.parseDouble(parts[1]);
            String date = parts[2];
            String country = parts[3];
            String channel = parts[4];
            String accountId = parts[5];

            Tranzactie tranzactie = new Tranzactie(id, amount, date, country, channel, accountId);
            tranzactii.add(tranzactie);
        }

        int q = Integer.parseInt(readNonEmptyLine(sc));

        for (int i = 0; i < q; i++) {
            String line = readNonEmptyLine(sc);
            String[] parts = line.trim().split("\\s+");

            String command = parts[0];

            switch (command) {
                case "REPORT_MONTH" -> {
                    String month = parts[1];
                    reportMonth(tranzactii, month);
                }

                case "REPORT_ACCOUNT" -> {
                    String accountId = parts[1];
                    reportAccount(tranzactii, accountId);
                }

                case "TOP_CHANNELS" -> {
                    int k = Integer.parseInt(parts[1]);
                    topChannels(tranzactii, k);
                }

                default -> {
                }
            }
        }
    }
    private static String readNonEmptyLine(Scanner sc){
        while (sc.hasNextLine()){
            String linie = sc.nextLine().trim();
            if (!linie.isEmpty()) return linie;
        }
        return "";
    }
    private static void reportMonth(List<Tranzactie> tranzactii, String month){
//        int nr = 0;
//        double total = 0;
//        for (Tranzactie t: tranzactii){
//            if (t.getDate().substring(0,7).equals(month)) {
//                total += t.getAmount();
//                nr += 1;
//            }
//        }

        Map<String, DoubleSummaryStatistics> byMonth = tranzactii.stream().collect(Collectors.groupingBy(
                t->t.getDate().substring(0,7), Collectors.summarizingDouble(t -> t.getAmount())));

        DoubleSummaryStatistics statistica = byMonth.get(month);

        if (statistica == null) {
            System.out.printf(Locale.US, "MONTH %s total=%.2f count=%d%n", month, 0.0, 0);
        }
        else{
            System.out.printf(Locale.US, "MONTH %s total=%.2f count=%d%n", month, statistica.getSum(), statistica.getCount());
        }
    }
    private static void reportAccount(List<Tranzactie> tranzactii, String accountId){
//        int nr = 0;
//        double total = 0;
//
//        for (Tranzactie t: tranzactii){
//            if (t.getAccountId().equals(accountId)){
//                total += t.getAmount();
//                nr += 1;
//            }
//        }
        Map<String, DoubleSummaryStatistics> byAccId = tranzactii.stream().collect(Collectors.groupingBy(
                t -> t.getAccountId(), Collectors.summarizingDouble(t->t.getAmount())
        ));
        DoubleSummaryStatistics statistica = byAccId.get(accountId);
        if (statistica == null)
            System.out.printf(Locale.US, "ACCOUNT %s total=%.2f count=%d%n", accountId, 0.0, 0);
        else{
            System.out.printf(Locale.US, "ACCOUNT %s total=%.2f count=%d%n", accountId, statistica.getSum(), statistica.getCount());
        }
    }
    private static void topChannels(List<Tranzactie> tranzactii, int k){
        Map<String, Long> statistica = tranzactii.stream().collect(Collectors.groupingBy(tx -> tx.getChannel(), Collectors.counting()));

        if (statistica.isEmpty()) {
            System.out.println("NONE");
            return;
        }

        statistica.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(k).forEach(e -> System.out.println(e.getKey() + " " + e.getValue()));
    }

}
