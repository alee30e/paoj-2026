package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

public final class CustomCollectors {

    private CustomCollectors() {
    }

    public static Collector<Tranzactie, ?, Snapshot> toSnapshot(int topN) {
        class Agg {
            private final Map<String, Long> countByCountry = new HashMap<>();
            private final Map<String, Long> countByChannel = new HashMap<>();
            private BigDecimal totalAmount = BigDecimal.ZERO;
            private final List<Tranzactie> allTranzactii = new ArrayList<>();

            void add(Tranzactie tx) {
                countByCountry.merge(tx.getCountry(), 1L, Long::sum);
                countByChannel.merge(tx.getChannel(), 1L, Long::sum);

                totalAmount = totalAmount.add(tx.getAmount());

                allTranzactii.add(tx);
            }

            Agg combine(Agg other) {
                mergeCounts(this.countByCountry, other.countByCountry);
                mergeCounts(this.countByChannel, other.countByChannel);

                this.totalAmount = this.totalAmount.add(other.totalAmount);

                this.allTranzactii.addAll(other.allTranzactii);

                return this;
            }

            Snapshot finish() {
                List<Tranzactie> top = allTranzactii.stream()
                        .sorted(Comparator.comparing(Tranzactie::getAmount).reversed().thenComparing(Tranzactie::getDate)
                                        .thenComparing(Tranzactie::getId))
                        .limit(topN).toList();

                return new Snapshot(countByCountry, countByChannel, totalAmount, top);
            }
        }

        return Collector.of(Agg::new, Agg::add, Agg::combine, Agg::finish);
    }

    private static void mergeCounts(Map<String, Long> destination, Map<String, Long> source) {
        source.forEach((key, value) -> destination.merge(key, value, Long::sum));
    }
}