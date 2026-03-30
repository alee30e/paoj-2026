package com.pao.laboratory05.audit;

import com.pao.laboratory05.audit.Angajat;

import java.time.LocalDateTime;
import java.util.Arrays;

public class AngajatService {
    private AuditEntry[] auditlog = new AuditEntry[0];
    private Angajat[] angajati = new Angajat[0];
    private AngajatService() {}
    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }
    public static AngajatService getInstance() {
        return AngajatService.Holder.INSTANCE;
    }
    public void addAngajat(Angajat a) {
        Angajat[] nou = Arrays.copyOf(angajati, angajati.length + 1);
        nou[angajati.length] = a;
        angajati = nou;
        System.out.println("Angajat: " + a.getNume());
        logAction("ADD", a.getNume());
    }
    public void listBySalary() {
        if (angajati.length == 0) {
            System.out.println("Nu exista");
            return;
        }

        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);

        System.out.println("Salariu - descrescator");
        for (int i = 0; i < copy.length; i++) {
            System.out.println((i + 1) + ". " + copy[i]);
        }
    }

    public void findByDepartament(String numeDept) {
        logAction("FIND_BY_DEPT", numeDept);
        boolean gasit = false;

        System.out.println("Departament " + numeDept);
        for (Angajat angajat : angajati) {
            if (angajat.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(angajat);
                gasit = true;
            }
        }
        if (!gasit) {
            System.out.println("Nu exista ang in " + numeDept);
        }
    }
    public void printAuditLog() {
        if (auditlog.length == 0) {
            System.out.println("Nu exista");
            return;
        }
        System.out.println("Audit log:");
        for (AuditEntry entry : auditlog) {
            System.out.println(entry);
        }
    }
    private void logAction(String action, String target) {
        AuditEntry entry = new AuditEntry(action, target, LocalDateTime.now().toString());

        AuditEntry[] nou = Arrays.copyOf(auditlog, auditlog.length + 1);
        nou[auditlog.length] = entry;
        auditlog = nou;
    }
}