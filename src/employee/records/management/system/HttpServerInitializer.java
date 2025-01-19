package employee.records.management.system;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpServerInitializer {

    public static void startServer() throws IOException {
        
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        
        server.createContext("/employees", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response = "Employee Records API is running!";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        // Start the server
        System.out.println("HTTP server is running on port 8081...");
        server.start();
    }
}
