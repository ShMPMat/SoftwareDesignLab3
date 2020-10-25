package ru.akirakozov.sd.refactoring.servlet;

import net.sourceforge.jwebunit.junit.WebTester;
import net.sourceforge.jwebunit.util.TestingEngineRegistry;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.akirakozov.sd.refactoring.Main;
import ru.akirakozov.sd.refactoring.RunTestServer;

import java.sql.*;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


@PowerMockIgnore({ "javax.net.ssl.*", "javax.security.*", "jdk.internal.reflect.*" })
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DriverManager.class, GetProductsServlet.class })
public class GetProductsServletTest {
    private static final String WEBSITE_URL = "http://localhost:8081";

    private WebTester webTester;
    private ResultSet resultQuerySet;

    @BeforeClass
    public static void runServer() {
        RunTestServer.run();
    }

    @Before
    public void start() throws SQLException {
        webTester = new WebTester();
        webTester.setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
        webTester.getTestContext().setBaseUrl(WEBSITE_URL);

        final Statement statement = mock(Statement.class);
        final Connection connection = mock(Connection.class);
        resultQuerySet = mock(ResultSet.class);
        mockStatic(DriverManager.class);

        when(connection.createStatement())
                .thenReturn(statement);
        when(DriverManager.getConnection("jdbc:sqlite:test.db"))
                .thenReturn(connection);
        when(statement.executeQuery("SELECT * FROM PRODUCT"))
                .thenReturn(resultQuerySet);
    }

    @Test
    public void connectionTest() throws SQLException {
        when(resultQuerySet.next())
                .thenReturn(false);

        webTester.beginAt("/get-products");
        webTester.assertResponseCode(200);
    }

    @Test
    public void oneProductTest() throws SQLException {
        String[] names = { "iphone6" };
        int[] prices = { 100 };
        when(resultQuerySet.next())
                .thenReturn(true)
                .thenReturn(false);
        when(resultQuerySet.getString("name"))
                .thenReturn(names[0]);
        when(resultQuerySet.getInt("price"))
                .thenReturn(prices[0]);

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
        String[] names = { "iphone6", "oven", "milk" };
        int[] prices = { 100, 10, 10012 };
        when(resultQuerySet.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        when(resultQuerySet.getString("name"))
                .thenReturn(names[0])
                .thenReturn(names[1])
                .thenReturn(names[2]);
        when(resultQuerySet.getInt("price"))
                .thenReturn(prices[0])
                .thenReturn(prices[1])
                .thenReturn(prices[2]);

        webTester.beginAt("/get-products");

        for (String name : names) {
            webTester.assertTextPresent(name);
        }
        for (int price : prices) {
            webTester.assertTextPresent(Integer.toString(price));
        }
    }
}
