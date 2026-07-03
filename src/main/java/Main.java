/**
 * Main application class.
 * Runs a simulated library flow without waiting for user input,
 * making it fully compatible with automated CI/CD pipeline builds.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   LIBRARY MANAGEMENT SYSTEM - SIMULATION RUN    ");
        System.out.println("=================================================");

        // 1. Initialize Library
        System.out.println("[INFO] Initializing Library database...");
        Library library = new Library();

        // 2. Add Books
        System.out.println("[INFO] Adding sample books to library catalog...");
        Book book1 = new Book("978-0134685991", "Effective Java", "Joshua Bloch");
        Book book2 = new Book("978-0132350884", "Clean Code", "Robert C. Martin");
        Book book3 = new Book("978-0135957059", "The Pragmatic Programmer", "Andy Hunt");

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        System.out.println("[SUCCESS] Total books in catalog: " + library.getBooksCount());
        
        // Print catalog
        System.out.println("\n--- Current Library Catalog ---");
        for (Book book : library.getAllBooks()) {
            System.out.println(" -> " + book);
        }

        // 3. Borrowing a Book (Success Path)
        String borrowIsbn = "978-0132350884"; // Clean Code
        System.out.println("\n[ACTION] Attempting to borrow 'Clean Code' (ISBN: " + borrowIsbn + ")...");
        boolean isBorrowed = library.borrowBook(borrowIsbn);
        if (isBorrowed) {
            System.out.println("[SUCCESS] Book borrowed successfully.");
        } else {
            System.out.println("[ERROR] Borrow failed!");
        }

        // Print status of borrowed book
        System.out.println("[STATUS] Borrowed book state: " + library.findBook(borrowIsbn));

        // 4. Borrowing the Same Book (Failure Path / Edge Case)
        System.out.println("\n[ACTION] Attempting to borrow 'Clean Code' again to test double checkout protection...");
        boolean isDoubleBorrowed = library.borrowBook(borrowIsbn);
        if (!isDoubleBorrowed) {
            System.out.println("[SUCCESSFUL FAILURE] Borrow rejected as expected. Book is already checked out.");
        } else {
            System.out.println("[BUG] Book was double borrowed! Thread safety or logic failure.");
        }

        // 5. Returning the Book (Success Path)
        System.out.println("\n[ACTION] Returning 'Clean Code' (ISBN: " + borrowIsbn + ")...");
        boolean isReturned = library.returnBook(borrowIsbn);
        if (isReturned) {
            System.out.println("[SUCCESS] Book returned and catalog updated.");
        } else {
            System.out.println("[ERROR] Return failed!");
        }

        // Print final status of book
        System.out.println("[STATUS] Returned book state: " + library.findBook(borrowIsbn));

        System.out.println("\n=================================================");
        System.out.println("   SIMULATION COMPLETED - PIPELINE SUCCESSFUL    ");
        System.out.println("=================================================");
    }
}
