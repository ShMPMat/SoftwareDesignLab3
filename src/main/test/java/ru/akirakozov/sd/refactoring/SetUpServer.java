package ru.akirakozov.sd.refactoring;

import net.sourceforge.jwebunit.junit.WebTester;
import net.sourceforge.jwebunit.util.TestingEngineRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.fail;

public class SetUpServer {
    private static final String WEBSITE_URL = "http://localhost:8081";

    public static void runServer() {
        Thread serverThread = new Thread(() -> {
            try {
                Main.main(new String[0]);
            } catch (Exception e) {
                fail("Cannot start server");
            }
        });
        serverThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Cannot start server");
        }
    }

    public static WebTester setUpWebTester() {
        WebTester webTester = new WebTester();
        webTester.setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
        webTester.getTestContext().setBaseUrl(WEBSITE_URL);

        return webTester;
    }

    public static Connection setUpTestDbConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:unitTest.db");

        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM PRODUCT");
        statement.close();

        return DriverManager.getConnection("jdbc:sqlite:unitTest.db");
    }
}
