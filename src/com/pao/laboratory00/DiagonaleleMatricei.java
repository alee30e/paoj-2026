package com.pao.laboratory00;

import java.util.Scanner;

/**
 * Exercitiul 2
 *
 * Cititi de la tastatura o matrice de n ori n elemente REALE.
 *
 * 1. Afisati matricea in consola.
 * 2. Afisati suma elementelor de pe diagonala principala
 *    si produsul elementelor de pe diagonala secundara.
 *
 */

public class DiagonaleleMatricei {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n, suma = 0, prod = 1;
        n = scanner.nextInt();
        int m[][];
        m = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                m[i][j] = scanner.nextInt();
                if (i == j) suma += m[i][j];
                if ((i + j) == (n - 1)) prod *= m[i][j];
            }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                System.out.print(m[i][j] + " ");
            System.out.println();
        }

        System.out.println("Suma elem de pe diag principala: " + suma);
        System.out.println("Produsul elem de pe diag secundara: " + prod);
    }
}
