package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dbms.DbmsFactory;
import ru.akirakozov.sd.refactoring.html.HtmlResponseWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        HtmlResponseWriter writer = new HtmlResponseWriter(response);

        switch (request.getParameter("command")) {
            case "max":
                doGetMax(writer);
                break;
            case "min":
                doGetMin(writer);
                break;
            case "sum":
                doGetSum(writer);
                break;
            case "count":
                doGetCount(writer);
                break;
            default:
                writer.printText("Unknown command: " + command);
        }

        writer.end();
    }

    private void doGetMax(HtmlResponseWriter writer) throws IOException {
        writer.printHeader("Product with max price: ");
        writer.printProducts(DbmsFactory.getDbms().getProductMax());
    }

    private void doGetMin(HtmlResponseWriter writer) throws IOException {
        writer.printHeader("Product with min price: ");
        writer.printProducts(DbmsFactory.getDbms().getProductMin());
    }

    private void doGetSum(HtmlResponseWriter writer) throws IOException {
        writer.printText("Summary price: ");
        writer.printText(String.valueOf(DbmsFactory.getDbms().getProductPricesSum()));
    }

    private void doGetCount(HtmlResponseWriter writer) throws IOException {
        writer.printText("Number of products: ");
        writer.printText(String.valueOf(DbmsFactory.getDbms().getProductCount()));
    }
}
