/**
 * Encapusulates information about a book.
 * @author 
 */
public class Book {
    String title;
    String author;
    String isbn;
    int publicationYear;
    // number of copies in the library
    // NOTE: This is not the number of copies available in the library
    int numberOfCopies; 
    int copiesav;

    /**
     * Constructor. Most properties (except number of copies are read only)
     */
    public Book(String title, String author, String isbn, int publicationYear, int numberOfCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.numberOfCopies = numberOfCopies;
        this.copiesav = numberOfCopies;
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
     * @return The number of copies of this book.
     */
    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    /**
     * Adds the given mumber of copies of this book to the library.
     */
    public void addCopies(int numCopiesToAdd) {
        numberOfCopies += numCopiesToAdd;
        copiesav += numCopiesToAdd;
    }

    /** 
     * Checks out a book (decrements number of copies available in the library)
     * @throws RuntimeException if no copies are available to check out.
     */
    public void checkout() {
        if(copiesav == 0)
            throw new IllegalStateException("There are no copies :(");
        else
            copiesav--;
    } 

    /** 
     * Checks in a book into the library.
     * @throws RuntimeException if no copies have been checked out.
     */
    public void checkin() {
        if(copiesav == numberOfCopies)
            throw new IllegalStateException("No copies have been checked out :/");
        else
            copiesav++;
    }

    @Override
    public int hashCode() {
        int result = 11;     
        
        result = 31 * result + (title  != null ? title.hashCode()  : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (isbn   != null ? isbn.hashCode()   : 0);
        
        return result;
    }

    @Override
    public boolean equals(Object that) {
        // NOTE: Two books are the same only if the Title, Author, and ISBN matches
        if (this == that) return true;
        if (!(that instanceof Book)) return false;
        Book that = (Book)that;
        return this.author.equals(that.getAuthor()) && this.title.equals(that.getTitle()) && this.isbn.equals(that.getIsbn())
    }
}
