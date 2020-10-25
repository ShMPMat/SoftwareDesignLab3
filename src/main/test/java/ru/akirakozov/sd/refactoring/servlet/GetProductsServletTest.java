package ru.akirakozov.sd.refactoring.servlet;

import net.sourceforge.jwebunit.junit.WebTester;
import net.sourceforge.jwebunit.util.TestingEngineRegistry;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.akirakozov.sd.refactoring.RunTestServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


@PowerMockIgnore({"javax.net.ssl.*", "javax.security.*", "jdk.internal.reflect.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({DriverManager.class, GetProductsServlet.class, AddProductServlet.class})
public class GetProductsServletTest {
    private static final String WEBSITE_URL = "http://localhost:8081";

    private WebTester webTester;
    private Connection connection;

    @BeforeClass
    public static void runServer() {
        RunTestServer.run();
    }

    @Before
    public void start() throws SQLException {
        webTester = new WebTester();
        webTester.setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
        webTester.getTestContext().setBaseUrl(WEBSITE_URL);
        connection = DriverManager.getConnection("jdbc:sqlite:unitTest.db");

        String sql = "DELETE FROM PRODUCT";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();

        Connection testConnection = DriverManager.getConnection("jdbc:sqlite:unitTest.db");
        mockStatic(DriverManager.class);
        when(DriverManager.getConnection("jdbc:sqlite:test.db"))
                .thenReturn(testConnection);
    }

    @Test
    public void connectionTest() throws SQLException {
        webTester.beginAt("/get-products");
        webTester.assertResponseCode(200);
    }

    @Test
    public void oneProductTest() throws SQLException {
        String[] names = {"iphone6"};
        int[] prices = {100};
        String sql = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + names[0] + "\"," + prices[0] + ")";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();

        webTester.beginAt("/get-products");

        for (String name : names) {
            webTester.assertTextPresent(name);
        }
        for (int price : prices) {
            webTester.assertTextPresent(Integer.toString(price));
        }
    }

    @Test
    public void manyProductsTest() throws SQLException {
        String[] names = {"iphone6", "oven", "milk"};
        int[] prices = {100, 10, 10012};
        String sql = "INSERT INTO PRODUCT (NAME, PRICE) VALUES " +
                "(\"" + names[0] + "\"," + prices[0] + ")," +
                "(\"" + names[1] + "\"," + prices[1] + ")," +
                "(\"" + names[2] + "\"," + prices[2] + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();

        webTester.beginAt("/get-products");

        for (String name : names) {
            webTester.assertTextPresent(name);
        }
        for (int price : prices) {
            webTester.assertTextPresent(Integer.toString(price));
        }
    }
}
