import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of Books.
 * Demonstrates collection handling (ArrayList) and business logic encapsulation.
 */
public class Library {
    private List<Book> books;

    /**
     * Constructor initializing the internal collection of books.
     */
    public Library() {
        this.books = new ArrayList<>();
    }

    /**
     * Adds a new book to the library database.
     *
     * @param book The book object to add
     */
    public void addBook(Book book) {
        if (book != null) {
            books.add(book);
        }
    }

    /**
     * Searches for a book in the library using its unique ISBN.
     *
     * @param isbn Unique 13/10 digit identifier
     * @return Book if found, null otherwise
     */
    public Book findBook(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    /**
     * Handles borrowing a book by search of its ISBN.
     * Updates the book's availability status.
     *
     * @param isbn The ISBN of the book to borrow
     * @return true if borrowing succeeded, false if book not found or already borrowed
     */
    public boolean borrowBook(String isbn) {
        Book book = findBook(isbn);
        if (book != null) {
            return book.borrowBook();
        }
        return false; // Book not found
    }

    /**
     * Handles returning a borrowed book by search of its ISBN.
     * Updates the book's availability status.
     *
     * @param isbn The ISBN of the book to return
     * @return true if returning succeeded, false if book not found or already returned
     */
    public boolean returnBook(String isbn) {
        Book book = findBook(isbn);
        if (book != null) {
            return book.returnBook();
        }
        return false; // Book not found
    }

    /**
     * Returns the total count of books in the library.
     * Used for testing build success.
     *
     * @return Number of books
     */
    public int getBooksCount() {
        return books.size();
    }

    /**
     * Returns a copy of the internal book list.
     *
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }
}
