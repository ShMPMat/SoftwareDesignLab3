package ru.akirakozov.sd.refactoring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.fail;

public class QueryUtil {
    public static void executeQuery(String sqlQuery) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:unitTest.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlQuery);
            statement.close();
        } catch (SQLException e) {
            fail();
        }
    }
}
