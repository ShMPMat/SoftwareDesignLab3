package ru.akirakozov.sd.refactoring.servlet;

import net.sourceforge.jwebunit.junit.WebTester;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.akirakozov.sd.refactoring.SetUpServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@PowerMockIgnore({"javax.net.ssl.*", "javax.security.*", "jdk.internal.reflect.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({DriverManager.class, GetProductsServlet.class, AddProductServlet.class, QueryServlet.class})
public class QueryServletTest {
    private WebTester webTester;
    private Connection connection;

    @BeforeClass
    public static void runServer() {
        SetUpServer.runServer();
    }

    @Before
    public void start() throws SQLException {
        webTester = SetUpServer.setUpWebTester();
        connection = SetUpServer.setUpTestDbConnection();

        Connection testConnection = DriverManager.getConnection("jdbc:sqlite:unitTest.db");
        mockStatic(DriverManager.class);
        when(DriverManager.getConnection("jdbc:sqlite:test.db"))
                .thenReturn(testConnection);
    }


    @Test
    public void connectionTest() {
        webTester.beginAt("/query?command=max");
        webTester.assertResponseCode(200);
    }

    @Test
    public void singleProductMaxTest() throws SQLException {
        String[] names = {"iphone6"};
        int[] prices = {10};
        String sql = "INSERT INTO PRODUCT (NAME, PRICE) VALUES " +
                "(\"" + names[0] + "\"," + prices[0] + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();

        webTester.beginAt("/query?command=max");

        webTester.assertTextPresent(names[0]);
        webTester.assertTextPresent(Integer.toString(prices[0]));
    }

    @Test
    public void manyProductsMaxTest() throws SQLException {
        String[] names = {"iphone6", "oven", "milk"};
        int[] prices = {100, 10, 10012};
        String sql = "INSERT INTO PRODUCT (NAME, PRICE) VALUES " +
                "(\"" + names[0] + "\"," + prices[0] + ")," +
                "(\"" + names[1] + "\"," + prices[1] + ")," +
                "(\"" + names[2] + "\"," + prices[2] + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();

        webTester.beginAt("/query?command=max");

        webTester.assertTextPresent(names[2]);
        webTester.assertTextPresent(Integer.toString(prices[2]));
    }

    @Test
    public void singleProductMinTest() throws SQLException {
        String[] names = {"iphone6"};
        int[] prices = {10};
        String sql = "INSERT INTO PRODUCT (NAME, PRICE) VALUES " +
                "(\"" + names[0] + "\"," + prices[0] + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();

        webTester.beginAt("/query?command=min");

        webTester.assertTextPresent(names[0]);
        webTester.assertTextPresent(Integer.toString(prices[0]));
    }

    @Test
    public void manyProductsMinTest() throws SQLException {
        String[] names = {"iphone6", "oven", "milk"};
        int[] prices = {100, 10, 10012};
        String sql = "INSERT INTO PRODUCT (NAME, PRICE) VALUES " +
                "(\"" + names[0] + "\"," + prices[0] + ")," +
                "(\"" + names[1] + "\"," + prices[1] + ")," +
                "(\"" + names[2] + "\"," + prices[2] + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();

        webTester.beginAt("/query?command=min");

        webTester.assertTextPresent(names[1]);
        webTester.assertTextPresent(Integer.toString(prices[1]));
    }

    @Test
    public void singleSumTest() throws SQLException {
        String[] names = {"iphone6"};
        int[] prices = {10};
        String sql = "INSERT INTO PRODUCT (NAME, PRICE) VALUES " +
                "(\"" + names[0] + "\"," + prices[0] + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();

        webTester.beginAt("/query?command=sum");

        webTester.assertTextPresent("10");
    }

    @Test
    public void manyProductsSumTest() throws SQLException {
        String[] names = {"iphone6", "oven", "milk"};
        int[] prices = {100, 10, 10000};
        String sql = "INSERT INTO PRODUCT (NAME, PRICE) VALUES " +
                "(\"" + names[0] + "\"," + prices[0] + ")," +
                "(\"" + names[1] + "\"," + prices[1] + ")," +
                "(\"" + names[2] + "\"," + prices[2] + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();

        webTester.beginAt("/query?command=sum");

        webTester.assertTextPresent("10110");
    }

    @Test
    public void singleCountTest() throws SQLException {
        String[] names = {"iphone6"};
        int[] prices = {10};
        String sql = "INSERT INTO PRODUCT (NAME, PRICE) VALUES " +
                "(\"" + names[0] + "\"," + prices[0] + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();

        webTester.beginAt("/query?command=count");

        webTester.assertTextPresent("1");
    }

    @Test
    public void manyProductsCountTest() throws SQLException {
        String[] names = {"iphone6", "oven", "milk"};
        int[] prices = {100, 10, 10000};
        String sql = "INSERT INTO PRODUCT (NAME, PRICE) VALUES " +
                "(\"" + names[0] + "\"," + prices[0] + ")," +
                "(\"" + names[1] + "\"," + prices[1] + ")," +
                "(\"" + names[2] + "\"," + prices[2] + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();

        webTester.beginAt("/query?command=count");

        webTester.assertTextPresent("3");
    }
}
