package ru.akirakozov.sd.refactoring.dbms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dbms {
    private String databasePath;

    public Dbms(String databasePath) {
        this.databasePath = databasePath;
    }

    public void addProduct(Product product) {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                String sql = "INSERT INTO PRODUCT " +
                        "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")";
                try (Statement stmt = c.createStatement()) {
                    stmt.executeUpdate(sql);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getProducts() {
        return getProductsFromQuery("SELECT * FROM PRODUCT");
    }

    public List<Product> getProductMax() {
        return getProductsFromQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
    }

    public List<Product> getProductMin() {
        return getProductsFromQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
    }

    public int getProductCount() {
        try {
            try (Connection c = DriverManager.getConnection(databasePath)) {
                try (Statement stmt = c.createStatement()) {
                    try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT")) {
                        if (rs.next()) {
                            return rs.getInt(1);
                        } else {
                            throw new RuntimeException("No result for Count");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getProductPricesSum() {
        try {
            try (Connection c = DriverManager.getConnection(databasePath)) {
                try (Statement stmt = c.createStatement()) {
                    try (ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT")) {
                        if (rs.next()) {
                            return rs.getInt(1);
                        } else {
                            throw new RuntimeException("No result for Sum");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<Product> getProductsFromQuery(String sql) {
        try {
            try (Connection c = DriverManager.getConnection(databasePath)) {
                try (Statement stmt = c.createStatement()) {
                    try (ResultSet rs = stmt.executeQuery(sql)) {
                        return getProductsFromResultSet(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Product> getProductsFromResultSet(ResultSet resultSet) throws SQLException {
        List<Product> products = new ArrayList<>();

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int price = resultSet.getInt("price");
            products.add(new Product(name, price));
        }

        return products;
    }
}
