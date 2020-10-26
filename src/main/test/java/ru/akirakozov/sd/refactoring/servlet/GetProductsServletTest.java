package ru.akirakozov.sd.refactoring.servlet;

import net.sourceforge.jwebunit.junit.WebTester;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.akirakozov.sd.refactoring.QueryUtil;
import ru.akirakozov.sd.refactoring.TestServerSetUpUtil;
import ru.akirakozov.sd.refactoring.dbms.Dbms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


@PowerMockIgnore({"javax.net.ssl.*", "javax.security.*", "jdk.internal.reflect.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({DriverManager.class, GetProductsServlet.class, AddProductServlet.class, QueryServlet.class, Dbms.class})
public class GetProductsServletTest {
    private WebTester webTester;
    private Connection connection;

    @BeforeClass
    public static void runServer() {
        TestServerSetUpUtil.runServer();
    }

    @Before
    public void start() throws SQLException {
        webTester = TestServerSetUpUtil.setUpWebTester();
        connection = TestServerSetUpUtil.setUpTestDbConnection();

        Connection testConnection = DriverManager.getConnection("jdbc:sqlite:unitTest.db");
        mockStatic(DriverManager.class);
        when(DriverManager.getConnection("jdbc:sqlite:test.db"))
                .thenReturn(testConnection);
    }


    @Test
    public void connectionTest() {
        webTester.beginAt("/get-products");
        webTester.assertResponseCode(200);
    }

    @Test
    public void oneProductTest() {
        String[] names = {"iphone6"};
        int[] prices = {100};
        QueryUtil.executeQuery(
                "INSERT INTO PRODUCT (NAME, PRICE) VALUES " +
                        "(\"" + names[0] + "\"," + prices[0] + ")"
        );

        webTester.beginAt("/get-products");

        for (String name : names) {
            webTester.assertTextPresent(name);
        }
        for (int price : prices) {
            webTester.assertTextPresent(Integer.toString(price));
        }
    }

    @Test
    public void manyProductsTest() {
        String[] names = {"iphone6", "oven", "milk"};
        int[] prices = {100, 10, 10012};
        QueryUtil.executeQuery(
                "INSERT INTO PRODUCT (NAME, PRICE) VALUES " +
                        "(\"" + names[0] + "\"," + prices[0] + ")," +
                        "(\"" + names[1] + "\"," + prices[1] + ")," +
                        "(\"" + names[2] + "\"," + prices[2] + ")"
        );

        webTester.beginAt("/get-products");

        for (String name : names) {
            webTester.assertTextPresent(name);
        }
        for (int price : prices) {
            webTester.assertTextPresent(Integer.toString(price));
        }
    }
}
