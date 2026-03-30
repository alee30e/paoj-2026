package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti = new Carte[0];
    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }
    public static BibliotecaService getInstance() {
        return Holder.INSTANCE;
    }
    private BibliotecaService() {};
    public void addCarte(Carte carte) {
        Carte[] temp = new Carte[carti.length + 1];
        System.arraycopy(carti, 0, temp, 0, carti.length);
        temp[carti.length] = carte;
        carti = temp;
        System.out.println("Carte: " + carte);
    }

    public void listSortedByRating() {
        Carte[] copy = carti.clone();
        Arrays.sort(copy);
        for (Carte carte : copy) {
            System.out.println(carte);
        }
    }

    public void listSortedBy(Comparator<Carte> comparator) {
        Carte[] copy = carti.clone();
        Arrays.sort(copy, comparator);
        for (Carte carte : copy) {
            System.out.println(carte);
        }
    }
}
