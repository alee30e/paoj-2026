package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;
import com.pao.laboratory09.exercise1.Tranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

import static com.pao.laboratory09.exercise1.TipTranzactie.CREDIT;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;
    private static byte statusToByte (String status){
        if (status.equals("PROCESSED")) return 1;
        else if (status.equals("REJECTED")) return 2;
        else return 0;
    }

    private static void printRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE);

        byte[] record = new byte[RECORD_SIZE];
        raf.readFully(record);

        ByteBuffer bb = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);

        int id = bb.getInt();
        double suma = bb.getDouble();

        byte[] dataBytes = new byte[10];
        bb.get(dataBytes);
        String data = new String(dataBytes).trim();

        byte tipByte = bb.get();
        byte statusByte = bb.get();

        String tip;
        if (tipByte == 0) tip = "CREDIT";
        else
            tip = "DEBIT";
        String status = switch (statusByte) {
            case 1 -> "PROCESSED";
            case 2 -> "REJECTED";
            default -> "PENDING";
        };

        System.out.printf("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n",
                idx, id, data, tip, suma, status);
    }

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md

        Scanner sc = new Scanner(System.in);

        int N = Integer.parseInt(sc.nextLine());

        List<Tranzactie> lista = new ArrayList<>();

        for (int i =0; i < N; i++){
            String linie = sc.nextLine();

            String[] cuv = linie.split(" ");
            int id = Integer.parseInt(cuv[0]);
            String data = cuv[2];
            double suma = Double.parseDouble(cuv[1]);
            TipTranzactie tip = TipTranzactie.valueOf(cuv[3]);

            Tranzactie t = new Tranzactie(id, suma, data,"", "", tip);

            lista.add(t);
        }

        new File("output").mkdirs();

        FileOutputStream file = new FileOutputStream(OUTPUT_FILE);
        DataOutputStream fout = new DataOutputStream(file);

        for (Tranzactie t: lista){
            byte[] idBytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(t.getId()).array();
            fout.write(idBytes);

            byte[] sumaBytes = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(t.getSuma()).array();
            fout.write(sumaBytes);

            byte[] dataBytes = new byte[10];
            byte[] raw = t.getData().getBytes();
            System.arraycopy(raw, 0, dataBytes,0, raw.length);
            fout.write(dataBytes);

            byte tipByte;
            if (t.getTip() == CREDIT)
                tipByte = 0;
            else
                tipByte = 1;
            fout.write(tipByte);

            fout.write(0);

            fout.write(new byte[8]);
        }
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format binar, RECORD_SIZE=32 bytes/înreg.)
        //    - bytes 0-3:   id (int, little-endian via ByteBuffer)
        //    - bytes 4-11:  suma (double, little-endian via ByteBuffer)
        //    - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        //    - byte 22:     tip (0=CREDIT, 1=DEBIT)
        //    - byte 23:     status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        //    - bytes 24-31: padding (zerouri)
        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx       → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        //    - UPDATE idx ST  → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        //                       afișează "Updated [idx]: STATUS"
        //    - PRINT_ALL      → citește și afișează toate înregistrările
        //
        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>


        RandomAccessFile raf = new RandomAccessFile("output/lab09_ex2.bin", "rw");
        while (sc.hasNextLine()) {
            String linie = sc.nextLine();
            if (linie.isEmpty()) continue;
            String[] cuv = linie.split(" ");

            if (cuv[0].equals("READ")){
                int id = Integer.parseInt(cuv[1]);

                raf.seek(id * RECORD_SIZE);
                byte[] record = new byte[32];
                raf.read(record);
                printRecord(raf, id);
            }
            else if (cuv[0].equals("UPDATE")){
                int id =Integer.parseInt(cuv[1]);
                String st = cuv[2];
                raf.seek(id * RECORD_SIZE + 23);
                raf.write(statusToByte(st));

                System.out.println("Updated [" + id + "]: " + st);
            }
            else if (cuv[0].equals("PRINT_ALL")){
                for (int i = 0; i < N; i++){
                    printRecord(raf, i);
                }
            }
        }
//        System.out.println("TODO: implementează exercițiul 2");
    }
}
