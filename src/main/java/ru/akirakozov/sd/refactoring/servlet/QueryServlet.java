package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dbms.DbmsFactory;
import ru.akirakozov.sd.refactoring.dbms.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
        writer.println("<html><body>");
        writer.println("<h1>Product with max price: </h1>");

        for (Product product : DbmsFactory.getDbms().getProductMax()) {
            writer.println(product.getName() + "\t" + product.getPrice() + "</br>");
        }
        writer.println("</body></html>");
    }

    private void doGetMin(PrintWriter writer) {
        writer.println("<html><body>");
        writer.println("<h1>Product with min price: </h1>");

        for (Product product : DbmsFactory.getDbms().getProductMin()) {
            writer.println(product.getName() + "\t" + product.getPrice() + "</br>");
        }
        writer.println("</body></html>");
    }

    private void doGetSum(PrintWriter writer) {
        writer.println("<html><body>");
        writer.println("Summary price: ");
        writer.println(DbmsFactory.getDbms().getProductPricesSum());
        writer.println("</body></html>");
    }

    private void doGetCount(PrintWriter writer) {
        writer.println("<html><body>");
        writer.println("Number of products: ");
        writer.println(DbmsFactory.getDbms().getProductCount());
        writer.println("</body></html>");
    }
}
