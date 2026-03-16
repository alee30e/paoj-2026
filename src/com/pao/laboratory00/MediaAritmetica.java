package com.pao.laboratory00;

import java.util.Scanner;

/**
 * Exercitiul 1
 *
 * Cititi de la tastatura un sir cu n elemente intregi.
 *
 * 1. Afisati elementele sirului in doua modalitati.
 * 2. Afisati media aritmetica a elementelor sirului.
 *
 */

public class MediaAritmetica {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n, suma = 0;
        n = scanner.nextInt();
        int[] a;
        a = new int[n];
        for (int i = 0; i < n; i++){
            a[i] = scanner.nextInt();
            suma += a[i];
        }
        System.out.println("Prima afisare: ");
        for (int i = 0; i < a.length; i++)
            System.out.println(a[i]);
        System.out.println("A doua afisare: ");
        for (int i : a)
            System.out.print(i + " ");
        System.out.println();
        System.out.println("Media aritmetica: " + (double) suma / n);
    }
}
