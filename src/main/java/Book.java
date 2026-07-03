/**
 * Represents a Book in the Library Management System.
 * Demonstrates basic encapsulation and Object-Oriented Programming (OOP) principles.
 */
public class Book {
    private String isbn;
    private String title;
    private String author;
    private boolean isAvailable;

    /**
     * Constructor to initialize a Book object.
     * By default, a new book is available.
     *
     * @param isbn   Unique identifier for the book (e.g., "123-456")
     * @param title  The title of the book
     * @param author The author of the book
     */
    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.isAvailable = true; // Book is available by default when added to library
    }

    // Getters and Setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * Changes availability status of the book to false (borrowed).
     *
     * @return true if borrowing was successful, false if it was already borrowed
     */
    public boolean borrowBook() {
        if (isAvailable) {
            isAvailable = false;
            return true;
        }
        return false;
    }

    /**
     * Changes availability status of the book to true (returned).
     *
     * @return true if returning was successful, false if the book was already available
     */
    public boolean returnBook() {
        if (!isAvailable) {
            isAvailable = true;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + isbn + '\'' +
                ", Title='" + title + '\'' +
                ", Author='" + author + '\'' +
                ", Available=" + isAvailable +
                '}';
    }
}
