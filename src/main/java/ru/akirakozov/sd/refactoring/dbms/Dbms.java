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
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");
                if (rs.next()) {
                    int count = rs.getInt(1);
                    rs.close();
                    stmt.close();
                    return count;
                } else {
                    rs.close();
                    stmt.close();
                    throw new RuntimeException("No result for Count");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
