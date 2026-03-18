package com.pao.laboratory03.exercise.model;

import com.pao.laboratory03.exercise.exception.InvalidGradeException;
import com.pao.laboratory03.exercise.exception.InvalidStudentException;

import java.util.HashMap;
import java.util.Map;

public class Student {
    String name;
    int age;
    Map<Subject, Double> grades;
    public Student(String name, int age){
        this.name = name;
        this.age = age;
        grades = new HashMap<>();
        if (age < 18 || age > 60)
            throw new InvalidStudentException("Varsta trebuie sa fie intre 18 si 60");
    }
    public int getAge(){
        return age;
    }
    public String getName(){
        return name;
    }
    public Map<Subject, Double> getGrades(){
        return grades;
    }
    public void addGrade(Subject subject, double grade){
        if (grade < 0 || grade > 10)
            throw new InvalidGradeException("Nota nu e valida");
        grades.put(subject, grade);
    }
    public double getAverage(){
        if (grades.isEmpty()) return 0;
        double suma = 0;
        for (double grade: grades.values()){
            suma += grade;
        }
        return suma/grades.size();
    }
    @Override
    public String toString(){
        return("Student{name= " + getName() + ", age=" + getAge() + ", avg=" + getAverage() +"}");
    }
}
