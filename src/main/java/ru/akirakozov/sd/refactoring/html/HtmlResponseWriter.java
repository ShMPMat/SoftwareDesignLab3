package ru.akirakozov.sd.refactoring.html;

import ru.akirakozov.sd.refactoring.dbms.Product;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class HtmlResponseWriter {
    private HttpServletResponse response;

    public HtmlResponseWriter(HttpServletResponse response) throws IOException {
        this.response = response;
        this.response.getWriter().println("<html><body>");
        this.response.setContentType("text/html");
    }

    public void printText(String string) throws IOException {
        response.getWriter().println(string);
    }

    public void printHeader(String header) throws IOException {
        printText("<h1>" + header + "</h1>");
    }

    public void printProducts(List<Product> products) throws IOException {
        for (Product product : products) {
            printText(product.getName() + "\t" + product.getPrice() + "</br>");
        }
    }

    public void end() throws IOException {
        response.getWriter().println("</body></html>");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
