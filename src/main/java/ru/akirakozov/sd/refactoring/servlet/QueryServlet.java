package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        switch (request.getParameter("command")) {
            case "max":
                doGetMax(response.getWriter());
                break;
            case "min":
                doGetMin(response.getWriter());
                break;
            case "sum":
                doGetSum(response.getWriter());
                break;
            case "count":
                doGetCount(response.getWriter());
                break;
            default:
                response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void doGetMax(PrintWriter writer) {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
                writer.println("<html><body>");
                writer.println("<h1>Product with max price: </h1>");

                while (rs.next()) {
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    writer.println(name + "\t" + price + "</br>");
                }
                writer.println("</body></html>");

                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doGetMin(PrintWriter writer) {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
                writer.println("<html><body>");
                writer.println("<h1>Product with min price: </h1>");

                while (rs.next()) {
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    writer.println(name + "\t" + price + "</br>");
                }
                writer.println("</body></html>");

                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doGetSum(PrintWriter writer) {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
                writer.println("<html><body>");
                writer.println("Summary price: ");

                if (rs.next()) {
                    writer.println(rs.getInt(1));
                }
                writer.println("</body></html>");

                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doGetCount(PrintWriter writer) {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");
                writer.println("<html><body>");
                writer.println("Number of products: ");

                if (rs.next()) {
                    writer.println(rs.getInt(1));
                }
                writer.println("</body></html>");

                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
