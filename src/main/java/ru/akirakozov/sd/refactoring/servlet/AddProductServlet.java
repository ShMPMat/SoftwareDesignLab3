package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dbms.DbmsFactory;
import ru.akirakozov.sd.refactoring.dbms.Product;
import ru.akirakozov.sd.refactoring.html.HtmlResponseWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        DbmsFactory.getDbms().addProduct(new Product(name, price));

        HtmlResponseWriter writer = new HtmlResponseWriter(response);
        writer.printText("OK");
        writer.end();
    }
}
