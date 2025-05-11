import java.util.Objects;

/**
 * Encapusulates information about a book.
 * @author Balaji 
 */
public class Book {
    String title;
    String author;
    String isbn;
    int publicationYear;
    // number of copies currently available in the library
    int numberOfCopies;
    // total number of copies the library owns
    private int totalCopies;

    /**
     * Constructor. Most properties (except number of copies are read only)
     */
    public Book(String title, String author, String isbn, int publicationYear, int numberOfCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.numberOfCopies = numberOfCopies;
        this.totalCopies = numberOfCopies;
    }

    /**
     * @return The title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The author of the book.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return the ISBN for this book.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @return The publication year of this book.
     */
    public int getPublicationYear() {
        return publicationYear;
    }

    /**
     * @return The number of copies of this book currently available.
     */
    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    /**
     * Adds the given number of copies of this book to the library.
     * @param numCopiesToAdd how many copies to add
     */
    public void addCopies(int numCopiesToAdd) {
        numberOfCopies += numCopiesToAdd;
        totalCopies += numCopiesToAdd;
    }

    /**
     * Checks out a book (decrements number of copies available in the library).
     * @throws RuntimeException if no copies are available to check out.
     * @author Rylen
     */
    public void checkout() {
        if (numberOfCopies <= 0) {
            throw new RuntimeException("No copies available for checkout");
        }
        numberOfCopies--;
    }

    /**
     * Checks in a book into the library.
     * @throws RuntimeException if no copies have been checked out.
     * @author Rylen
     */
    public void checkin() {
        if (numberOfCopies >= totalCopies) {
            throw new RuntimeException("All copies are already in the library");
        }
        numberOfCopies++;
    }

    /**
     * Computes a hash code consistent with equals, based on title, author, and ISBN
     * @return hash code for this book
     * @author Rylen
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, author, isbn);
    }

    /**
     * Two books are the same only if the title, author, and ISBN match
     * @param obj other object to compare
     * @return true if this and obj represent the same book (title, author, isbn)
     * @author Rylen
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Book)) return false;
        Book that = (Book) obj; 
        return Objects.equals(title, that.title)
            && Objects.equals(author, that.author)
            && Objects.equals(isbn, that.isbn);
    }
}
