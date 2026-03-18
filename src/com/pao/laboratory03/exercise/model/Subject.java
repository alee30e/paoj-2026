package com.pao.laboratory03.exercise.model;

public enum Subject {
    PAOJ("Programare Avansata pe Obiecte - Java", 6),
    BD("Baze de Date", 5),
    SO("Sisteme de Operare", 4),
    RC("Retele de Calculatoare", 5);
    private final String fullName;
    private final int credits;

    private Subject(String fullName, int credits){
        this.credits = credits;
        this.fullName = fullName;
    }

    public int getCredits(){
        return credits;
    }
    public String getFullName(){
        return fullName;
    }
    @Override
    public String toString(){
        return name() + " (" + fullName + ", " + credits + " credite)";
    }
}
