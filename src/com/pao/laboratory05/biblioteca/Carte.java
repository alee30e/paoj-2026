package com.pao.laboratory05.biblioteca;

public class Carte implements Comparable<Carte>{
    private String titlu;
    private String autor;
    private int an;
    private double rating;

    public Carte(String titlu, String autor, int an, double rating){
        this.titlu = titlu;
        this.autor = autor;
        this.an = an;
        this.rating = rating;
    }
    public String getTitlu(){
        return this.titlu;
    }
    public String getAutor(){
        return this.autor;
    }
    public int getAn(){
        return this.an;
    }
    public double getRating(){
        return this.rating;
    }
    @Override
    public String toString(){
        return "Carte{titlu='" + titlu + "', autor='" + autor +  "', an=" + an + ", rating=" + rating +"}";
    }
    @Override
    public int compareTo(Carte other){
        return Double.compare(other.getRating(), this.getRating());
    }
}
