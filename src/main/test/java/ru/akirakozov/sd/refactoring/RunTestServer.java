package ru.akirakozov.sd.refactoring;

import static org.junit.Assert.fail;

public class RunTestServer {
    public static void run() {
        Thread serverThread = new Thread(() -> {
            try {
                Main.main(new String[0]);
            } catch (Exception e) {
                fail("Cannot start server");
            }
        });
        serverThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Cannot start server");
        }
    }
}
