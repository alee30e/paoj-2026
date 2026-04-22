package com.pao.laboratory08.exercise1;

import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";


    public static List<Student> citire() throws IOException{
        List<Student> studenti = new ArrayList<>();
        BufferedReader fin = new BufferedReader(new FileReader(FILE_PATH));
        String linie;

        while ((linie = fin.readLine()) != null){
//            System.out.println(linie);
            String[] cuv = linie.split(",");
//            System.out.println(cuv);
            if (cuv.length >= 4) {
                String nume = cuv[0].trim();
                int varsta = Integer.parseInt(cuv[1].trim());
                String oras = cuv[2].trim();
                String strada = cuv[3].trim();
                Adresa adresa = new Adresa(oras, strada);
                Student student = new Student(nume, varsta, adresa);

                studenti.add(student);
//                System.out.println(student);
            }
        }
        fin.close();
        return studenti;
    }

    public static Student gasire(List<Student> studenti, String nume){
        for (Student student : studenti){
            if (student.getNume().equals(nume)){
                return student;
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește comanda din stdin: PRINT, SHALLOW <nume> sau DEEP <nume>
        // 3. Execută comanda:
        //    - PRINT → afișează toți studenții
        //    - SHALLOW <nume> → shallow clone + modifică orașul clonei la "MODIFICAT" + afișează
        //    - DEEP <nume> → deep clone + modifică orașul clonei la "MODIFICAT" + afișeaz
        Scanner sc = new Scanner(System.in);
        String comanda = sc.nextLine().trim();
        List<Student> studenti = citire();
        if (comanda.equals("PRINT")){
            for (Student student : studenti){
                System.out.println(student);
            }
        }
        else {
            String[] cuv = comanda.split(" ", 2);
            String tip = cuv[0];
            String nume = cuv[1];

            Student original = gasire(studenti, nume);
            if (original == null){
                return;
            }
            Student clona;
            if (tip.equals("SHALLOW")){
                clona=original.shallowClone();
                clona.getAdresa().setOras("MODIFICAT");
                System.out.println("Original: "+original);
                System.out.println("Clona: "+clona);
            }
            else if (tip.equals("DEEP")){
                clona = original.deepClone();
                clona.getAdresa().setOras("MODIFICAT");
                System.out.println("Original: "+original);
                System.out.println("Clona: "+clona);
            }

        }

//        System.out.println("TODO: implementează exercițiul 1");
    }
}
