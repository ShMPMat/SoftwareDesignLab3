package ru.akirakozov.sd.refactoring.dbms;

public class DbmsFactory {
    public static Dbms getDbms() {
        return new Dbms("jdbc:sqlite:test.db");
    }
}
