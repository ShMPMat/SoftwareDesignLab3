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
public class GetProductsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HtmlResponseWriter writer = new HtmlResponseWriter(response);
        writer.printProducts(DbmsFactory.getDbms().getProducts());
        writer.end();
    }
}
