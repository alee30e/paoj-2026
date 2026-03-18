package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;

import java.util.*;

public class StudentService {
    private List<Student> students;
    private static StudentService instance;
    private StudentService(){
        students = new ArrayList<>();
    }
    public static StudentService getInstance(){
        if (instance == null)
            instance = new StudentService();
        return instance;
    }
    public void addStudent(String name, int age){
        Student s = new Student(name, age);
        for (Student s1 : students){
            if (s.getName().equalsIgnoreCase(s1.getName()))
                throw new RuntimeException("Exista un student cu acelasi nume");
        }
        students.add(s);
    }
    Student findByName(String name){
        for (Student s: students){
            if (s.getName().equalsIgnoreCase(name))
                return s;
        }
        throw new StudentNotFoundException("Studentul cu acest nume nu exista");
    }
    public void addGrade(String studentName, Subject subject, double grade){
        Student s = findByName(studentName);
        s.addGrade(subject, grade);
    }
    public void printAllStudents(){
        int i = 0;
        for (Student s: students){
            i += 1;
            System.out.println(i +" " + s);
            for (Map.Entry<Subject, Double> entry: s.getGrades().entrySet())
                System.out.println("Subject: "+ entry.getKey() + ", Nota: " + entry.getValue());
            System.out.println();
        }
    }
    public void printTopStudents(){
        List<Student> st = new ArrayList<>(students);
        st.sort((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()));

        for (Student s : st)
            System.out.println(s);
    }
    public Map<Subject, Double> getAveragePerSubject(){
        Map<Subject, List<Double>> note = new HashMap<>();
        for (Student s: students){
            for (Map.Entry<Subject, Double> entry: s.getGrades().entrySet()){
                Subject subject = entry.getKey();
                Double grade = entry.getValue();
                if (!note.containsKey(subject))
                    note.put(subject, new ArrayList<>());

                note.get(subject).add(grade);
            }
        }
        Map<Subject, Double> subjectAvg = new HashMap<>();
        for (Map.Entry<Subject, List<Double>> entry: note.entrySet()){
            Subject subject = entry.getKey();
            List<Double> listaNote = entry.getValue();

            double suma = 0, medie = 0;
            for (Double nota: listaNote)
                suma += nota;
            medie = suma / listaNote.size();

            subjectAvg.put(subject, medie);
        }
        return subjectAvg;
    }
}

