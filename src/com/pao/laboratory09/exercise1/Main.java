package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        Scanner sc = new Scanner(System.in);

        int N = Integer.parseInt(sc.nextLine());

        List<Tranzactie> lista = new ArrayList<>();

        for (int i =0; i < N; i++){
            String linie = sc.nextLine();

            String[] cuv = linie.split(" ");
            int id = Integer.parseInt(cuv[0]);
            String data = cuv[2];
            double suma = Double.parseDouble(cuv[1]);
            String contSursa = cuv[3];
            String contDestinatie = cuv[4];
            TipTranzactie tip = TipTranzactie.valueOf(cuv[5]);

            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);

            lista.add(t);
        }

        for (Tranzactie t : lista) {
            t.setNote("procesat");
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            out.writeObject(lista);
        }

        List<Tranzactie> listaDeserializata;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            listaDeserializata = (List<Tranzactie>) in.readObject();
        }

        while (sc.hasNextLine()){
            String linie = sc.nextLine();
            if (linie.isEmpty()) continue;

            if (linie.equals("LIST")){
                for (Tranzactie t : lista) System.out.println(t);
            }
            else if (linie.startsWith("FILTER")){
                String[] cuv = linie.split(" ");
                String data = cuv[1];

                boolean gasit = false;

                for (Tranzactie t : listaDeserializata) {
                    if (t.getData().startsWith(data)){
                        System.out.println(t);
                        gasit = true;
                    }
                }
                if (!gasit) System.out.println("Niciun rezultat.");
            }
            else if (linie.startsWith("NOTE")){
                String[] cuv = linie.split(" ");
                int id = Integer.parseInt(cuv[1]);

                boolean gasit = false;

                for (Tranzactie t : listaDeserializata){
                    if (t.getId() == id){
                        System.out.println("NOTE[" + id + "]: "+ t.getNote());
                        gasit = true;
                        break;
                    }
                }
                if (!gasit) System.out.println("NOTE["+id+"]: not found");
            }
        }
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa contDestinatie tip)


        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de serializare
        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream (try-with-resources)
        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream (try-with-resources)
        // 5. Procesează comenzile din stdin până la EOF:
        //    - LIST          → afișează toate tranzacțiile, câte una pe linie
        //    - FILTER yyyy-MM → afișează tranzacțiile cu data care începe cu yyyy-MM
        //                       sau "Niciun rezultat." dacă nu există
        //    - NOTE id        → afișează "NOTE[id]: <valoarea câmpului note>"
        //                       sau "NOTE[id]: not found" dacă id-ul nu există
        //
        // Format linie tranzacție:
        //   [id] data tip: suma RON | contSursa -> contDestinatie
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1



//        System.out.println("TODO: implementează exercițiul 1");
    }
}
