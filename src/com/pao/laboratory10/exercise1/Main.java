package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // Folosește LinkedList<Tranzactie> ca structură internă.
        // Citește comenzi din stdin până la EOF:
        //
        //   ENQUEUE id suma data tip   → addLast  (niciun output)
        //   DEQUEUE                    → removeFirst sau "Coada goala."
        //                                format: "Procesat: [id] data tip: suma RON"
        //   PUSH id suma data tip      → addFirst  (niciun output)
        //   POP                        → removeFirst sau "Coada goala."
        //                                format: "Extras: [id] data tip: suma RON"
        //   REMOVE_DEBIT               → Iterator.remove() pe toate DEBIT
        //                                afișează "Eliminat N tranzactii DEBIT."
        //   REMOVE_BELOW threshold     → Iterator.remove() pe suma < threshold
        //                                afișează "Eliminat N tranzactii sub threshold RON."
        //   PRINT                      → afișează toate, câte una pe linie
        //   SIZE                       → "Dimensiune coada: N"
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-10 CREDIT: 500.00 RON
        LinkedList<Tranzactie> listaTranzactii = new LinkedList<>();
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()){
            String linie = sc.nextLine();
            String[] cuv = linie.split(" ");

            if (cuv[0].equals("ENQUEUE") || cuv[0].equals("PUSH")) {

                int id = Integer.parseInt(cuv[1]);
                double suma = Double.parseDouble(cuv[2]);
                String data = cuv[3];

                TipTranzactie tip = TipTranzactie.valueOf(cuv[4]);
                Tranzactie t = new Tranzactie(id, suma, data, tip);
                if (cuv[0].equals("ENQUEUE")) listaTranzactii.addLast(t);
                else listaTranzactii.addFirst(t);
            }
            else if (cuv[0].equals("DEQUEUE")){
                try {
                    Tranzactie t = listaTranzactii.removeFirst();
                    System.out.println("Procesat: " + t);
                }
                catch (Exception e){
                    System.out.println("Coada goala.");
                }
            }
            else if (cuv[0].equals("SIZE")){
                System.out.println("Dimensiune coada: " + listaTranzactii.size());
            }
            else if (cuv[0].equals("REMOVE_BELOW")){
                int eliminate = 0;
                double suma = Double.parseDouble(cuv[1]);
                Iterator<Tranzactie> it = listaTranzactii.iterator();

                while (it.hasNext()){
                    Tranzactie t = it.next();

                    if (t.getSuma() < suma) {
                        it.remove();
                        eliminate += 1;
                    }
                }
                System.out.println("Eliminat "+ eliminate + " tranzactii sub " + String.format("%.2f", suma)+ " RON.");
            }
            else if (cuv[0].equals("PRINT")){
                for (Tranzactie t : listaTranzactii){
                    System.out.println(t);
                }
            }
            else if (cuv[0].equals("POP")){
                try {
                    Tranzactie t = listaTranzactii.removeFirst();
                    System.out.println("Extras: " + t);
                } catch (Exception e){
                    System.out.println("Coada goala.");
                }
            }
            else if (cuv[0].equals("REMOVE_DEBIT")){
                int eliminate = 0;
                Iterator<Tranzactie> it = listaTranzactii.iterator();

                while (it.hasNext()){
                    Tranzactie t = it.next();

                    if (t.getTip().equals(TipTranzactie.DEBIT)) {
                        it.remove();
                        eliminate += 1;
                    }
                }
                System.out.println("Eliminat "+ eliminate + " tranzactii DEBIT.");
            }

        }
//        System.out.println("TODO: implementează exercițiul 1");
    }
}
