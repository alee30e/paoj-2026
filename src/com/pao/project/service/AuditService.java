package com.pao.project.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class AuditService {
    private static AuditService instance;
    private final Path auditPath;

    private AuditService() {
        this.auditPath = Path.of("audit.csv");
    }

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public synchronized void logAction(String actionName) {
        try {
            boolean writeHeader = Files.notExists(auditPath) || Files.size(auditPath) == 0;

            try (BufferedWriter writer = Files.newBufferedWriter(
                    auditPath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            )) {
                if (writeHeader) {
                    writer.write("nume_actiune,timestamp");
                    writer.newLine();
                }

                writer.write(actionName + "," + LocalDateTime.now());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Nu s-a putut scrie in audit.csv.", e);
        }
    }
}
