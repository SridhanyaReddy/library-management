import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Unit Tests for Library Management System.
 * Demonstrates the "Test" stage of CI/CD pipelines.
 */
public class LibraryTest {

    private Library library;
    private Book book1;
    private Book book2;

    /**
     * Set up a clean database before running each test.
     * This avoids test pollution (test runs are independent).
     */
    @BeforeEach
    public void setUp() {
        library = new Library();
        book1 = new Book("111-222", "Effective Java", "Joshua Bloch");
        book2 = new Book("333-444", "Clean Code", "Robert C. Martin");
        
        library.addBook(book1);
        library.addBook(book2);
    }

    @Test
    @DisplayName("Should successfully add books and update library size")
    public void testAddBook() {
        assertEquals(2, library.getBooksCount(), "Library size should be 2 initially");
        
        Book book3 = new Book("555-666", "Design Patterns", "Gang of Four");
        library.addBook(book3);
        
        assertEquals(3, library.getBooksCount(), "Library size should increase to 3 after adding a book");
    }

    @Test
    @DisplayName("Should find an added book by its unique ISBN")
    public void testFindBookByIsbn() {
        Book found = library.findBook("111-222");
        assertNotNull(found, "Book should be found in catalog");
        assertEquals("Effective Java", found.getTitle(), "Found book title should match");
    }

    @Test
    @DisplayName("Should return null when searching for a non-existent ISBN")
    public void testFindBookByNonExistentIsbn() {
        Book found = library.findBook("999-999");
        assertNull(found, "Non-existent ISBN should return null");
    }

    @Test
    @DisplayName("Should successfully borrow an available book")
    public void testBorrowBookSuccess() {
        boolean result = library.borrowBook("111-222");
        assertTrue(result, "Borrowing an available book should return true");
        assertFalse(book1.isAvailable(), "Book availability status should be set to false");
    }

    @Test
    @DisplayName("Should fail when trying to borrow a book that is already checked out")
    public void testBorrowBookAlreadyBorrowed() {
        // First checkout (success)
        library.borrowBook("111-222");
        
        // Second checkout (should fail)
        boolean result = library.borrowBook("111-222");
        assertFalse(result, "Borrowing an already checked out book should return false");
    }

    @Test
    @DisplayName("Should successfully return a borrowed book")
    public void testReturnBookSuccess() {
        // Borrow first
        library.borrowBook("333-444");
        
        // Return book
        boolean result = library.returnBook("333-444");
        assertTrue(result, "Returning a borrowed book should return true");
        assertTrue(book2.isAvailable(), "Book availability status should reset to true");
    }

    @Test
    @DisplayName("Should return false when returning a book that is already in library")
    public void testReturnBookAlreadyReturned() {
        // Book is already available, trying to return it should fail
        boolean result = library.returnBook("333-444");
        assertFalse(result, "Returning a book that was never borrowed should return false");
    }
}
