package com.pao.laboratory08.exercise2;

import java.io.*;
import java.util.*;
import com.pao.laboratory08.exercise1.Student;
import com.pao.laboratory08.exercise1.Adresa;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";
    private static final String OUTPUT_FILE= "src/com/pao/laboratory08/exercise2/rezultat.txt";
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
    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește pragul de vârstă din stdin cu Scanner
        // 3. Filtrează studenții cu varsta >= prag
        // 4. Scrie filtrații în "rezultate.txt" cu BufferedWriter
        // 5. Afișează sumarul la consolă

        List<Student> studenti = citire();

        Scanner sc = new Scanner(System.in);
        int varsta = Integer.parseInt(sc.nextLine().trim());

        List<Student> studentiPrag = new ArrayList<>();
        for (Student student: studenti){
            if (student.getVarsta() >= varsta){
                studentiPrag.add(student);
            }
        }
        BufferedWriter fout = new BufferedWriter(new PrintWriter(OUTPUT_FILE));

        for(Student s : studentiPrag){
            fout.write(s.toString());
            fout.newLine();
        }

        fout.close();
        System.out.println("Filtru: varsta >= " + varsta);
        System.out.println("Rezultate: " + studentiPrag.size() + " studenti");
        System.out.println();

        for (Student s : studentiPrag) {
            System.out.println(s);
        }

        System.out.println();
//        System.out.println("TODO: implementează exercițiul 2");
    }
}

