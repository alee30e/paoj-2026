package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        int N;
        Scanner sc = new Scanner(System.in);
        N = Integer.parseInt(sc.nextLine());

        String[] cuv;
        String linie;

        ArrayList<Tranzactie> listaDuplicate = new ArrayList<>();
        LinkedHashSet<Integer> idUnice = new LinkedHashSet<>();
        TreeMap<String, double[]> raport = new TreeMap<>();

        for (int i = 0; i< N; i ++){

            linie = sc.nextLine();
            cuv = linie.split(" ");

            int id = Integer.parseInt(cuv[0]);
            double suma = Double.parseDouble(cuv[1]);
            String data = cuv[2];

            TipTranzactie tip = TipTranzactie.valueOf(cuv[3]);
            Tranzactie t = new Tranzactie(id, suma, data, tip);

            listaDuplicate.add(t);
            idUnice.add(id);
        }

        while (sc.hasNextLine()){
            linie = sc.nextLine();
            if (linie.equals("UNIQUE_IDS")){
                System.out.println("IDs unice (" + idUnice.size() + "): " + idUnice);
            }
            if (linie.equals("MONTHLY_REPORT")){
                for (Tranzactie t: listaDuplicate){
                    String luna = t.getData().substring(0,7);
                    if (!raport.containsKey(luna)){
                        raport.put(luna, new double[] {0, 0});
                    }
//                    double[] sume = raport.get(luna);
                    if (t.getTip() == TipTranzactie.CREDIT)
                        raport.get(luna)[0] += t.getSuma();
                    else raport.get(luna)[1] += t.getSuma();
                }
                Iterator<Map.Entry<String, double[]>> it = raport.entrySet().iterator();
                while (it.hasNext()){
                    Map.Entry<String, double[]> ent = it.next();

                    String luna = ent.getKey();
                    double[] sume = ent.getValue();

                    System.out.printf("%s: CREDIT %.2f RON, DEBIT %.2f RON%n", luna, sume[0], sume[1]);
                }
            }
            else if (linie.startsWith("TOP")){
                List<Tranzactie> copie = new ArrayList<>(listaDuplicate);
                int n = Integer.parseInt(linie.split(" ")[1]);
                Collections.sort(copie, Comparator.comparingDouble(Tranzactie::getSuma).reversed());

                List<Tranzactie> topN = copie.subList(0, n);

                System.out.println("Top " + n + ": ");
                for (Tranzactie t: topN){
                    System.out.println(t);
                }
            }
            else if (linie.equals("SORT_ASC")){
                Collections.sort(listaDuplicate, Comparator.comparingDouble(Tranzactie::getSuma));
                for (Tranzactie t: listaDuplicate)
                    System.out.println(t);
            }
            else if (linie.equals("SORT_DESC")){
                Collections.sort(listaDuplicate, Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                for (Tranzactie t: listaDuplicate)
                    System.out.println(t);
            }
            else if (linie.equals("REVERSE")){
                Collections.reverse(listaDuplicate);
                for (Tranzactie t: listaDuplicate)
                    System.out.println(t);
            }
            else if (linie.equals("MIN_MAX")){
                Tranzactie tMax = Collections.max(listaDuplicate, Comparator.comparingDouble(Tranzactie::getSuma));
                Tranzactie tMin = Collections.min(listaDuplicate, Comparator.comparingDouble(Tranzactie::getSuma));

                System.out.println("MIN: [" + tMin.getId() + "] " + tMin.getData() + " " + tMin.getTip() + ": " + String.format("%.2f", tMin.getSuma()) +" RON");
                System.out.println("MAX: [" + tMax.getId() + "] " + tMax.getData() + " " + tMax.getTip() + ": " + String.format("%.2f", tMax.getSuma()) + " RON");

            }
            else if (linie.equals("CME_DEMO")){
                try{
                    for (Tranzactie t : listaDuplicate) listaDuplicate.remove(t);
                } catch (ConcurrentModificationException e) {
                    System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                }
            }


        }
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip) — pot exista duplicate de id
        //    Stochează-le toate într-un ArrayList<Tranzactie> (cu duplicate, ordine inserare)
        //
        // 2. Procesează comenzile din stdin până la EOF:
        //
        //   UNIQUE_IDS      → LinkedHashSet<Integer> cu id-urile în ordinea primei apariții
        //                     afișează: "IDs unice (N): [1, 2, 3, ...]"
        //
        //   MONTHLY_REPORT  → TreeMap<String, ...> grupat pe yyyy-MM (substring 0-7 din data)
        //                     pentru fiecare lună, sumele CREDIT și DEBIT
        //                     format: "yyyy-MM: CREDIT X.XX RON, DEBIT Y.YY RON"
        //
        //   TOP n           → primele n tranzacții după suma descrescătoare (nu modifică lista)
        //                     afișează "Top n:" urmat de n linii
        //
        //   SORT_ASC        → Collections.sort cu suma crescătoare; afișează lista sortată
        //   SORT_DESC       → Collections.sort cu suma descrescătoare; afișează lista sortată
        //   REVERSE         → Collections.reverse; afișează lista
        //   MIN_MAX         → Collections.min/max după suma
        //                     "MIN: [id] data tip: suma RON"
        //                     "MAX: [id] data tip: suma RON"
        //
        //   CME_DEMO        → încearcă for(t : lista) lista.remove(t) în try-catch
        //                     afișează "ConcurrentModificationException prins: modificare in iteratie detectata."
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON

//        System.out.println("TODO: implementează exercițiul 2");
    }
}
