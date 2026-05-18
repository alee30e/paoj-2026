package com.pao.laboratory11.exercise1;

import java.util.*;
import java.util.function.Predicate;

public class Main {

    static int amountScore(Tranzactie t){
        if (t.getAmount() >= 5000) return 70;
        if (t.getAmount() >= 1000) return 40;
        if (t.getAmount() >= 500) return 20;
        if (t.getAmount() <= 100) return 5;
        return 0;
    }

    static int countryScore(Tranzactie t){
        if (countryInRisk.test(t)) return 25;
        return 0;
    }
    static int channelScore(Tranzactie t){
        return ChannelScore.getScoreChannel(t.getChannel());
    }

    static int totalScore(Tranzactie t){
        return amountScore(t) + countryScore(t) + channelScore(t);
    }

    static String verdict(Tranzactie t){
        if (flag.test(t)) return "FLAG";
        return "ALLOW";
    }

    static final int FLAG_THRESHOLD = 60;

    static final Set<String> HIGH_RISK_COUNTRIES = Set.of("RU", "NG", "IR", "KP", "SY");
    static final Predicate<Tranzactie> amountOverThreshold = tranzactie -> tranzactie.getAmount() >= 1000;
    static final Predicate<Tranzactie> countryInRisk = tranzactie -> HIGH_RISK_COUNTRIES.contains(tranzactie.getCountry());
    static final Predicate<Tranzactie> channelSuspicious = tranzactie -> ChannelScore.getSuspiciousChannel(tranzactie.getChannel());
    static final Predicate<Tranzactie> flag = tranzactie -> totalScore(tranzactie) >= FLAG_THRESHOLD;

    static final Comparator<Tranzactie> compareByIdScore = Comparator.comparingInt(Main::totalScore).reversed().thenComparingInt(t -> t.getId());

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        List<Tranzactie> tranzactii = new ArrayList<>();
        Map<Integer, Tranzactie> TranzactieById = new HashMap<>();

        int n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double amount = scanner.nextDouble();
            String date = scanner.next();
            String country = scanner.next();
            String channel = scanner.next();

            Tranzactie tranzactie = new Tranzactie(id, amount, date, country, channel);

            tranzactii.add(tranzactie);
            TranzactieById.put(id, tranzactie);
        }

        int q = scanner.nextInt();

        for (int i = 0; i < q; i++) {
            String comanda = scanner.next();

            switch (comanda) {
                case "CHECK": {
                    int id = scanner.nextInt();
                    Tranzactie tranzactie = TranzactieById.get(id);
                    if (tranzactie == null) {
                        System.out.println("CHECK " + id + " => NOT_FOUND");
                    } else {
                        int scor = totalScore(tranzactie);
                        System.out.println("CHECK " + id + " => " + verdict(tranzactie) + " score=" + scor);
                    }

                    break;
                }

                case "LIST_FLAGGED": {
                    int nr = 0;
                    for (Tranzactie t: tranzactii){
                        if (verdict(t).equals("FLAG")){
                            System.out.println("[" + t.getId() + "] FLAG score=" + totalScore(t));
                            nr += 1;
                        }
                    }
                    if (nr == 0) System.out.println("NONE");
                    break;
                }

                case "TOP_RISK": {
                    int k = scanner.nextInt();
                    if (k == 0) continue;
                    tranzactii.stream().sorted(compareByIdScore).limit(k).forEach(
                            tranzactie -> {
                                int score = totalScore(tranzactie);
                                String verdict = verdict(tranzactie);
                                System.out.println("["+tranzactie.getId()+"] "+ verdict +  " score="+score);
                            }
                    );
                    break;
                }

                default: {

                    System.out.println("ERR UNKNOWN_COMMAND");
                    break;
                }
            }
        }

        scanner.close();
    }
}