import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Main entry point.
 * Runs CLI Simulation (for Jenkins non-interactive builds) by default,
 * or starts a local HTTP Web Server with UI when run with "server" argument.
 */
public class Main {
    private static Library library = new Library();
    private static final int PORT = 8081; // Runs on port 8081 to avoid conflict with Jenkins (8080)

    public static void main(String[] args) {
        // Populate initial sample catalog
        library.addBook(new Book("978-0134685991", "Effective Java", "Joshua Bloch"));
        library.addBook(new Book("978-0132350884", "Clean Code", "Robert C. Martin"));
        library.addBook(new Book("978-0135957059", "The Pragmatic Programmer", "Andy Hunt"));

        // If the "server" argument is passed, launch the browser UI web server
        if (args.length > 0 && args[0].equalsIgnoreCase("server")) {
            startWebServer();
        } else {
            // Default: run the simulation (perfect for Jenkins pipeline as it exits cleanly)
            runCliSimulation();
        }
    }

    /**
     * Starts the built-in HTTP server on http://localhost:8081
     */
    private static void startWebServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/", new LibraryWebHandler());
            server.setExecutor(null); // default executor
            System.out.println("=================================================");
            System.out.println("🚀 Web Server started on: http://localhost:" + PORT);
            System.out.println("👉 Open this link in your browser to view the UI!");
            System.out.println("Press Ctrl+C in terminal to stop the server.");
            System.out.println("=================================================");
            server.start();
        } catch (IOException e) {
            System.err.println("Failed to start web server: " + e.getMessage());
        }
    }

    /**
     * Handler to process HTTP requests and serve HTML pages / process actions.
     */
    static class LibraryWebHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();

            // Process Actions
            if (path.equals("/borrow") && query != null) {
                String isbn = getQueryParam(query, "isbn");
                library.borrowBook(isbn);
                redirect(exchange, "/");
                return;
            } else if (path.equals("/return") && query != null) {
                String isbn = getQueryParam(query, "isbn");
                library.returnBook(isbn);
                redirect(exchange, "/");
                return;
            }

            // Generate Dashboard UI HTML Response
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Library Management</title>");
            html.append("<link href='https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap' rel='stylesheet'>");
            html.append("<style>");
            html.append("body { font-family: 'Outfit', sans-serif; background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 100%); color: #f1f5f9; min-height: 100vh; margin: 0; padding: 2rem; display: flex; flex-direction: column; align-items: center; }");
            html.append(".container { max-width: 900px; width: 100%; background: rgba(30, 41, 59, 0.7); backdrop-filter: blur(12px); border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 20px; padding: 2.5rem; box-shadow: 0 10px 30px rgba(0,0,0,0.5); }");
            html.append("h1 { text-align: center; font-weight: 700; background: linear-gradient(to right, #38bdf8, #818cf8); -webkit-background-clip: text; -webkit-text-fill-color: transparent; margin-bottom: 0.5rem; }");
            html.append(".subtitle { text-align: center; color: #94a3b8; font-size: 0.95rem; margin-bottom: 2rem; }");
            html.append("table { width: 100%; border-collapse: collapse; margin-top: 1rem; }");
            html.append("th { background: rgba(56, 189, 248, 0.15); color: #38bdf8; font-weight: 600; text-align: left; padding: 12px; border-bottom: 2px solid rgba(56, 189, 248, 0.3); }");
            html.append("td { padding: 16px 12px; border-bottom: 1px solid rgba(255, 255, 255, 0.05); }");
            html.append(".badge { display: inline-block; padding: 4px 10px; border-radius: 9999px; font-size: 0.75rem; font-weight: 600; text-transform: uppercase; }");
            html.append(".badge-avail { background: rgba(34, 197, 94, 0.2); color: #4ade80; border: 1px solid rgba(34, 197, 94, 0.4); }");
            html.append(".badge-borrowed { background: rgba(239, 68, 68, 0.2); color: #f87171; border: 1px solid rgba(239, 68, 68, 0.4); }");
            html.append(".btn { display: inline-block; text-decoration: none; padding: 6px 14px; border-radius: 8px; font-size: 0.85rem; font-weight: 600; transition: all 0.2s ease-in-out; }");
            html.append(".btn-borrow { background: #3b82f6; color: white; }");
            html.append(".btn-borrow:hover { background: #2563eb; transform: translateY(-1px); }");
            html.append(".btn-return { background: rgba(248, 113, 113, 0.15); color: #f87171; border: 1px solid rgba(248, 113, 113, 0.3); }");
            html.append(".btn-return:hover { background: rgba(248, 113, 113, 0.3); }");
            html.append("footer { margin-top: 2rem; text-align: center; font-size: 0.8rem; color: #64748b; }");
            html.append("</style></head><body>");

            html.append("<div class='container'>");
            html.append("<h1>📚 Library Management Dashboard</h1>");
            html.append("<div class='subtitle'>B.Tech DevOps Practical Examination Demonstration</div>");
            html.append("<div style='text-align: center; margin-top: -1.5rem; margin-bottom: 2rem; font-size: 0.95rem; color: #38bdf8; font-weight: 600;'>Candidate Name: Sridhanya Reddy Manda &nbsp;|&nbsp; Reg No: [Enter Register Number Here]</div>");

            html.append("<table>");
            html.append("<tr><th>ISBN</th><th>Title</th><th>Author</th><th>Status</th><th>Actions</th></tr>");

            for (Book book : library.getAllBooks()) {
                html.append("<tr>");
                html.append("<td>").append(book.getIsbn()).append("</td>");
                html.append("<td><strong>").append(book.getTitle()).append("</strong></td>");
                html.append("<td>").append(book.getAuthor()).append("</td>");
                if (book.isAvailable()) {
                    html.append("<td><span class='badge badge-avail'>Available</span></td>");
                    html.append("<td><a href='/borrow?isbn=").append(book.getIsbn()).append("' class='btn btn-borrow'>Borrow Book</a></td>");
                } else {
                    html.append("<td><span class='badge badge-borrowed'>Borrowed</span></td>");
                    html.append("<td><a href='/return?isbn=").append(book.getIsbn()).append("' class='btn btn-return'>Return Book</a></td>");
                }
                html.append("</tr>");
            }
            html.append("</table>");
            html.append("</div>");
            html.append("<footer>DevOps CI/CD Pipeline successfully executed this simulation instance</footer>");
            html.append("</body></html>");

            // Send HTTP response
            byte[] responseBytes = html.toString().getBytes("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }

        private String getQueryParam(String query, String paramName) {
            Map<String, String> params = new HashMap<>();
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    params.put(entry[0], entry[1]);
                }
            }
            return params.get(paramName);
        }

        private void redirect(HttpExchange exchange, String location) throws IOException {
            exchange.getResponseHeaders().set("Location", location);
            exchange.sendResponseHeaders(302, -1);
            exchange.close();
        }
    }

    /**
     * Default CLI simulation execution (used in CI pipeline).
     */
    private static void runCliSimulation() {
        System.out.println("=================================================");
        System.out.println("   LIBRARY MANAGEMENT SYSTEM - SIMULATION RUN    ");
        System.out.println("=================================================");
        System.out.println("[INFO] Running in Simulation Mode (exits cleanly for Jenkins)");
        System.out.println("[SUCCESS] Total books in catalog: " + library.getBooksCount());
        
        System.out.println("\n--- Current Library Catalog ---");
        for (Book book : library.getAllBooks()) {
            System.out.println(" -> " + book);
        }

        String isbn = "978-0132350884";
        System.out.println("\n[ACTION] Borrowing ISBN: " + isbn);
        library.borrowBook(isbn);
        System.out.println("[STATUS] Book state: " + library.findBook(isbn));

        System.out.println("\n[ACTION] Returning ISBN: " + isbn);
        library.returnBook(isbn);
        System.out.println("[STATUS] Book state: " + library.findBook(isbn));

        System.out.println("\n=================================================");
        System.out.println("   SIMULATION COMPLETED - PIPELINE SUCCESSFUL    ");
        System.out.println("=================================================");
    }
}
