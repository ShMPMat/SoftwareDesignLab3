package ru.akirakozov.sd.refactoring.dbms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Dbms {
    private String databasePath;

    public Dbms(String databasePath) {
        this.databasePath = databasePath;
    }

    public int countProducts() {
        try {
            try (Connection c = DriverManager.getConnection(databasePath)) {
                try (Statement stmt = c.createStatement()) {
                    try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT")) {
                        if (rs.next()) {
                            return rs.getInt(1);
                        } else {
                            throw new RuntimeException("No result for Count");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int sumProductPrices() {
        try {
            try (Connection c = DriverManager.getConnection(databasePath)) {
                try (Statement stmt = c.createStatement()) {
                    try (ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT")) {
                        if (rs.next()) {
                            return rs.getInt(1);
                        } else {
                            throw new RuntimeException("No result for Sum");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
