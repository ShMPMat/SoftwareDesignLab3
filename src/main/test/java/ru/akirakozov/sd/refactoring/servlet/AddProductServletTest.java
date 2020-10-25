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
import ru.akirakozov.sd.refactoring.Main;
import ru.akirakozov.sd.refactoring.RunTestServer;

import java.sql.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


@PowerMockIgnore({ "javax.net.ssl.*", "javax.security.*", "jdk.internal.reflect.*" })
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DriverManager.class, GetProductsServlet.class, AddProductServlet.class })
public class AddProductServletTest {
    private static final String WEBSITE_URL = "http://localhost:8081";

    private WebTester webTester;
    private Statement queryStatement;

    @BeforeClass
    public static void runServer() {
        RunTestServer.run();
    }

    @Before
    public void start() throws SQLException {
        webTester = new WebTester();
        webTester.setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
        webTester.getTestContext().setBaseUrl(WEBSITE_URL);

        queryStatement = mock(Statement.class);
        final Connection connection = mock(Connection.class);
        mockStatic(DriverManager.class);

        when(connection.createStatement())
                .thenReturn(queryStatement);
        when(DriverManager.getConnection("jdbc:sqlite:test.db"))
                .thenReturn(connection);
    }

    @Test
    public void connectionTest() {
        String name = "iphone2";
        String price = "300";
        webTester.beginAt("/add-product?name=" + name + "&price=" + price);

        webTester.assertResponseCode(200);
    }

    @Test
    public void productAdditionTest() throws SQLException {
        String name = "iphone2";
        String price = "300";
        webTester.beginAt("/add-product?name=" + name + "&price=" + price);

        webTester.assertResponseCode(200);

        verify(queryStatement)
                .executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"" + name + "\"," + price + ")");
    }
}
