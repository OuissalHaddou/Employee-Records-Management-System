package employee.records.management.system;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Start HTTP server for API testing
            HttpServerInitializer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Start your Swing application (if applicable)
        new Employees().setVisible(true);
    }
}