package com.pao.laboratory03.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Exercițiul 3 — Excepții (checked, unchecked, custom)
 *
 * Creează în acest pachet (lângă Main.java) două clase de excepții custom,
 * apoi demonstrează-le aici.
 *
 * PASUL 1 — Creează InvalidAgeException.java (fișier separat):
 *   - Extinde RuntimeException (unchecked)
 *   - Constructor cu String message → apelează super(message)
 *
 * PASUL 2 — Creează DuplicateEntryException.java (fișier separat):
 *   - Extinde RuntimeException (unchecked)
 *   - Constructor cu String message → apelează super(message)
 *
 * PASUL 3 — În acest Main.java, implementează și demonstrează:
 *
 *   a) UNCHECKED EXCEPTIONS — NullPointerException, ArrayIndexOutOfBoundsException:
 *      - Creează o metodă riskyMethod() care aruncă NullPointerException
 *      - Prinde-o cu try-catch, afișează mesajul erorii
 *      - Adaugă un bloc finally care se execută mereu
 *
 *   b) CUSTOM EXCEPTIONS — InvalidAgeException, DuplicateEntryException:
 *      - Creează o metodă validateAge(int age) care aruncă InvalidAgeException
 *        dacă age < 0 sau age > 150
 *      - Creează o metodă addToList(List<String> list, String name) care aruncă
 *        DuplicateEntryException dacă name există deja în listă
 *      - Demonstrează ambele cu try-catch
 *
 *   c) MULTI-CATCH:
 *      - Prinde InvalidAgeException | DuplicateEntryException într-un singur catch
 *
 *   d) CATCH ORDERING:
 *      - Demonstrează că prinderea specifică (InvalidAgeException) trebuie
 *        să fie ÎNAINTE de cea generală (RuntimeException)
 *
 *   e) THROW vs THROWS:
 *      - Creează o metodă cu semnătura: void process(int age) throws InvalidAgeException
 *      - Apeleaz-o din main cu try-catch
 *
 * Output așteptat:
 *
 * === a) Unchecked — NullPointerException ===
 * Prins: Cannot invoke "String.length()" because "s" is null
 * Finally se execută mereu!
 *
 * === b) Custom exceptions ===
 * InvalidAgeException: Vârsta -5 nu este validă (0-150)
 * DuplicateEntryException: 'Ana' există deja în listă
 *
 * === c) Multi-catch ===
 * Excepție prinsă: Vârsta 200 nu este validă (0-150)
 *
 * === d) Catch ordering (specific → general) ===
 * InvalidAgeException prinsă specific: Vârsta -1 nu este validă (0-150)
 *
 * === e) Throw vs throws ===
 * Metoda process() a aruncat: Vârsta 999 nu este validă (0-150)
 */
public class Main {
    private static void riskyMethod(){
        String s = null;
        System.out.println(s.length());
    }
    private static void validateAge(int age){
        if (age < 0 || age > 150){
            throw new InvalidAgeException("Varsta " + age + " nu e valida!");
        }
    }
    private static void addToList(List<String> list, String name) {
        for (String n : list) {
            if (n == name) {
                throw new DuplicateEntryException("DuplicateEntryException: Numele \'" + name + "\' exista deja");
            }
        }
    }
    private static void process(int age) throws InvalidAgeException {
        validateAge(age);
    }
    public static void main(String[] args) {
        // TODO: implementează pașii de mai sus
        System.out.println("a) Unchecked — NullPointerException");
        // Hint: creează mai întâi InvalidAgeException.java și DuplicateEntryException.java
        try {
            riskyMethod();
        } catch (NullPointerException e) {
            System.out.println("Prins: " + e.getMessage());
        } finally {
            System.out.println("Finally — se execută MEREU, chiar și cu return!");
        }

        System.out.println("b) Custom exceptions");
        try{
            validateAge(-5);
        } catch (InvalidAgeException e){
            System.out.println("InvalidAgeException: " + e.getMessage());
        }

        List<String> list = new ArrayList<>(Arrays.asList("Ana", "Bogdan", "Stefan"));
        try {
            addToList(list, "Ana");
        }
        catch (DuplicateEntryException e){
            System.out.println("DuplicateEntryException: "+ e.getMessage());
        }

        System.out.println("c) Multi-catch");
        try {
            int age = 200;
            String name = "Ale";
            addToList(list, "Ale");
            validateAge(age);
        }
        catch (DuplicateEntryException | InvalidAgeException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
        finally {
            System.out.println(list);
        }
        System.out.println("d) Catch ordering");
        try {
            validateAge(-1);
        } catch (InvalidAgeException e){
            System.out.println("Specific: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("General: " + e.getMessage());
        }

        System.out.println("e) Throw si Throws");
        try {
            process(999);
        } catch (InvalidAgeException e) {
            System.out.println("Metoda process() a aruncat: " + e.getMessage());
        }
    }
}

