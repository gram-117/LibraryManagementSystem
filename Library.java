import java.util.Scanner;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
* A library management class. Has a simple shell that users can interact with to add/remove/checkout/list books in the library.
* Also allows saving the library state to a file and reloading it from a file.
* 
* isbn is related a specific author and title. if a book has the same 
* title as a book that has already been entered but different isbn, 
* it is assumed to be an error. This is done as books with same title and author 
* which have signifcant differences within edition will likley have a difference 
* in title (or authors) to indicate so.
* ex: 
* Calculus 8th ed. by James Stewart, ISBN: 112 
* Calculus 9th ed. by James Stewart, ISBN: 114
* 
* if multiple copies of the same book (same isbn) are added but they have
* different publication dates, then all copies of the book return the 
* publication date of the first book added when .getPublicationYear() is 
* called
* 
* @author Josh, Graham, Aaron, Rylen.
*/


public class Library {

    // ISBN to Book map for O(1) lookups
    private Map<String, Book> booksByIsbn = new HashMap<>();
    // Tracks, for each title|author key, the next index to return (for duplicate title/author)
    private Map<String, String> isbnLookup = new HashMap<>();

    /**
     * Adds one copy of a book to the library. If the library already has this book then it
     * adds the number of copies the library has.
     * 
     * @author Graham 
     * @param book book to be added to library 
     */
    public void addBook(Book book) {
        // create new book if new book and add into ISBN lookup table 
        String isbnKey = book.getTitle() + "," + book.getAuthor();
        String isbn = book.getIsbn();
        // new new book then add to database
        if (!booksByIsbn.containsKey(isbn)) {
            isbnLookup.put(isbnKey, isbn);
            booksByIsbn.put(isbn, book); 
        } 
        // if title and author matches expected isbn add 1 copy
        else if ((book).equals(booksByIsbn.get(isbn))) {
            (booksByIsbn.get(isbn)).addCopies(1);
        } 
        else {
            // if title + author doesnt matched expected isbn throw error 
            throw new IllegalArgumentException(
                "ISBN doesnt match title-author pair: " + isbnKey + 
                ". Expected ISBN: " + isbnLookup.get(isbnKey) + 
                ", got: " + isbn);
        }
    }

    /**
     * Checks out the given book from the library. Throw the appropriate
     * exception if book doesnt exist or there are no more copies available.
     * 
     * @author 
     * @param isbn of the book to be checked out 
     * @throws NoSuchElementException if there are not copies in library 
     */
    public void checkout(String isbn) {
        Book b = findByISBN(isbn);
        if (b.getNumberOfCopies() <= 0) {
            throw new NoSuchElementException("No copies available...");
        }
        b.checkout();
        System.out.println("Checked out successfully: " + b.getTitle());
    }

    /**
     * Returns a book to the library.
     * 
     * @author Josh
     * @param isbn the ISBN of the book to return
     * @complexity O(1) average case (HashMap lookup + constant update)
     */
    public void returnBook(String isbn) {
        Book b = findByISBN(isbn);
        b.checkin();
        System.out.println("Returned successfully: " + b.getTitle());
    }

    /**
     * Finds a book by title and author. Throws if none exist.
     * 
     * @author Graham
     * @param title the title of the book to find
     * @param author the author of the book to find
     * @return the matching Book object
     * @throws NoSuchElementException if no matching book is found
     * @complexity O(1) HashMap look up 
     */
    public Book findByTitleAndAuthor(String title, String author) {
        String bookKey = title + "," + author;
        if (!isbnLookup.containsKey(bookKey)) {
            throw new NoSuchElementException("Book not found: " + bookKey);
        }
        return booksByIsbn.get(isbnLookup.get(bookKey));

    }

    /**
     * Finds this book in the library by ISBN. Throws if it doesn’t exist.
     * 
     * @author Graham
     * @param isbn the ISBN of the book to find
     * @return the matching Book object
     * @throws NoSuchElementException if the book is not found
     * @complexity O(1) average‐case (HashMap lookup)
     */
    public Book findByISBN(String isbn) {
        Book b = booksByIsbn.get(isbn);
        if (b == null) {
            throw new NoSuchElementException("Book not found: " + isbn);
        }
        return b;
    }

    /**
     * Saves the contents of this library to the given file in CSV format:
     * title,author,isbn,publicationYear,numberOfCopies
     * 
     * @author Graham
     * @param filename the path to the file where library data will be written
     * @throws RuntimeException if an I/O error occurs during saving
     * @complexity O(n) where n = total books
     */
    public void save(String filename) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(filename))) {
            for (Book b : booksByIsbn.values()) {
                w.write(String.join("-,-",
                    b.getTitle(),
                    b.getAuthor(),
                    b.getIsbn(),
                    String.valueOf(b.getPublicationYear()),
                    String.valueOf(b.getNumberOfCopies())
                ));
                w.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Invalid record format");
        }
    }

    /**
     * Loads the contents of this library from the given file. All existing data
     * in this library is cleared before loading. Expects the same CSV format
     * as produced by save().
     * 
     * @author Aaron
     * @param filename the path to the file from which library data will be read
     * @throws RuntimeException if an I/O error occurs or the file format is invalid
     * @complexity O(n) where n = number of records in file
     */
    public void load(String filename) {
        booksByIsbn.clear();
        isbnLookup.clear();
        try (BufferedReader r = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split("-,-", -1);
                if (parts.length != 5) {
                    throw new RuntimeException("Invalid record: " + line);
                }
                String title = parts[0];
                String author = parts[1];
                String isbn = parts[2];
                int year = Integer.parseInt(parts[3]);
                int copies = Integer.parseInt(parts[4]);
                String isbnKey = title + "," + author;
                Book newBook = new Book(title, author, isbn, year, copies);
                this.addBook(newBook);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading library from file: " + 
                filename, e);
        }
    }

    /**
     * Runs a simple shell that allows users to interact with the library.
     * The user can type the following commands:
     *   add <title> <author> <isbn> <year> 
     *   checkout <isbn>
     *   findByTitleAndAuthor <title> <author>
     *   return <isbn>
     *   list <isbn>
     *   save <filename>
     *   load <filename>
     *   exit
     * 
     * @author Aaron
     * @complexity O(1) per iteration for most commands
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        while (true) {
            System.out.print("library> ");
            String line = scanner.nextLine();

            if (line.startsWith("add")) {
                String[] parts = line.split(" ");
                if (parts.length != 5) {
                    System.out.println("Error: invalid add command");
                    continue;
                }
                String title = parts[1], author = parts[2], isbn = parts[3];
                int year;
                try {
                    year   = Integer.parseInt(parts[4]);
                } catch (NumberFormatException e) {
                    System.out.println("Error: invalid number format");
                    continue;
                }
                try { // add one copy at a time 
                    library.addBook(new Book(title, author, isbn, year, 1)); 
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }

            } else if (line.startsWith("checkout")) {
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    System.out.println("Error: invalid checkout command");
                    continue;
                }
                try {
                    library.checkout(parts[1]);
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }

            } else if (line.startsWith("findByTitleAndAuthor")) {
                String[] parts = line.split(" ");
                if (parts.length != 3) {
                    System.out.println("Error: invalid findByTitleAndAuthor command");
                    continue;
                }
                try {
                    Book b = library.findByTitleAndAuthor(parts[1], parts[2]);
                    System.out.printf("%s %d%n", b.getIsbn(), b.getNumberOfCopies());
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }

            } else if (line.startsWith("return")) {
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    System.out.println("Error: invalid return command");
                    continue;
                }
                try {
                    library.returnBook(parts[1]);
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }

            } else if (line.startsWith("list")) {
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    System.out.println("Error: invalid list command");
                    continue;
                }
                try {
                    Book b = library.findByISBN(parts[1]);
                    System.out.printf("%d%n", b.getNumberOfCopies());
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }

            } else if (line.startsWith("save")) {
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    System.out.println("Error: invalid save command");
                    continue;
                }
                try {
                    library.save(parts[1]);
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }

            } else if (line.startsWith("load")) {
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    System.out.println("Error: invalid load command");
                    continue;
                }
                try {
                    library.load(parts[1]);
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }

            } else if (line.startsWith("exit")) {
                break;
            }
        }

        scanner.close();
    }
}
