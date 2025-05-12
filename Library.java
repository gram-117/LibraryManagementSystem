import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A library management class. Has a simple shell that users can interact with to add/remove/checkout/list books in the library.
 * Also allows saving the library state to a file and reloading it from the file.
 * @author Josh, Graham, Aaron, Rylen.
 */
public class Library {

    // ISBN to Book map for O(1) lookups
    private Map<String, Book> booksByIsbn = new HashMap<>();
    // Tracks, for each title|author key, the next index to return (for your team's other methods)
    private Map<String, Integer> titleAuthorIndexMap = new HashMap<>();

    /**
     * Adds a book to the library. If the library already has this book then it
     * adds the number of copies.
     * @author Josh
     */
    public void addBook(Book book) {
        String isbn = book.getIsbn();
        if (booksByIsbn.containsKey(isbn)) {
            booksByIsbn.get(isbn).addCopies(book.getNumberOfCopies());
            System.out.println("Added more copies of existing book: " + book.getTitle());
        } else {
            booksByIsbn.put(isbn, book);
            System.out.println("Book added successfully: " + book.getTitle());
        }
    }

    /**
     * Checks out the given book from the library.
     * Throws RuntimeException if book doesn't exist or no copies available.
     * @author Josh
     */
    public void checkout(String isbn) {
        Book b = findByISBN(isbn);
        if (b == null) {
            throw new RuntimeException("Book with ISBN " + isbn + " not found.");
        }
        if (!b.isAvailable()) {
            throw new RuntimeException("No copies available for book: " + b.getTitle());
        }
        b.checkout();
        System.out.println("Checked out successfully: " + b.getTitle());
    }

    /**
     * Returns a book to the library.
     * Throws RuntimeException if book doesn't exist.
     * @author Josh
     */
    public void returnBook(String isbn) {
        Book b = findByISBN(isbn);
        if (b == null) {
            throw new RuntimeException("Book with ISBN " + isbn + " not found.");
        }
        b.checkin();
        System.out.println("Returned successfully: " + b.getTitle());
    }

    /**
     * Finds a book by title and author. Throws if none exist.  If multiple books
     * share the same title & author, successive calls cycle through them in
     * ascending ISBN order.
     * @author Graham
     */
    public Book findByTitleAndAuthor(String title, String author) {
        List<Book> matches = new ArrayList<>();
        for (Book b : booksByIsbn.values()) {
            if (b.getTitle().equals(title) && b.getAuthor().equals(author)) {
                matches.add(b);
            }
        }
        if (matches.isEmpty()) {
            throw new RuntimeException("Book not found: " + title + " by " + author);
        }
        // Deterministic order: sort by ISBN string (numeric order for these tests)
        matches.sort(Comparator.comparing(Book::getIsbn));

        String key = title + "|" + author;
        int idx = titleAuthorIndexMap.getOrDefault(key, 0);
        Book result = matches.get(idx % matches.size());
        titleAuthorIndexMap.put(key, idx + 1);
        return result;
    }

    /**
     * Finds this book in the library by ISBN. Throws if it doesn’t exist.
     * @author Graham
     */
    public Book findByISBN(String isbn) {
        Book b = booksByIsbn.get(isbn);
        if (b == null) {
            throw new RuntimeException("Book not found: " + isbn);
        }
        return b;
    }

    /**
     * Saves the contents of this library to the given file in CSV format:
     * title,author,isbn,publicationYear,numberOfCopies
     * @author Graham
     */
    public void save(String filename) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(filename))) {
            for (Book b : booksByIsbn.values()) {
                w.write(String.join(",",
                    b.getTitle(),
                    b.getAuthor(),
                    b.getIsbn(),
                    String.valueOf(b.getPublicationYear()),
                    String.valueOf(b.getNumberOfCopies())
                ));
                w.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving library to file: " + filename, e);
        }
    }

    /**
     * Loads the contents of this library from the given file. All existing data
     * in this library is cleared before loading. Expects the same CSV format
     * as produced by save().
     * @author Aaron
     */
    public void load(String filename) {
        booksByIsbn.clear();
        try (BufferedReader r = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length != 5) {
                    throw new RuntimeException("Invalid record: " + line);
                }
                String title = parts[0];
                String author = parts[1];
                String isbn = parts[2];
                int year = Integer.parseInt(parts[3]);
                int copies = Integer.parseInt(parts[4]);
                booksByIsbn.put(isbn, new Book(title, author, isbn, year, copies));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading library from file: " + filename, e);
        }
    }
    /**
     * @author Aaron Angeles 
     * @param args
     */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.print("library> ");
			String line = scanner.nextLine();
			// TODO: Implement code 
			if (line.startsWith("add")) {
				// TODO: Implement this case.
				// The format of the line is
				// add title author isbn publicationYear numberOfCopies
				// e.g. add Star_Trek Gene_Roddenberry ISBN-1234 1965 10
				// NOTE: If a book already exists in the library, then the number of copies should be incremented by this amount.
				// Do appropriate error checking here.
			} else if (line.startsWith("checkout")) {
				// TODO: Implement this case.
				// The format of the line is
				// checkout isbn
				// e.g. checkout ISBN-1234
				// NOTE: If the book doesnt exist in the library, then the code should print an error.
			} else if (line.startsWith("findByTitleAndAuthor")) {
				// TODO: Implement this case.
				// The format of the line is
				// findByTitleAndAuthor <title> <author>
				// e.g. findByTitleAndAuthor Star_Trek Gene_Roddenberry
				// NOTE: If the book doesnt exist in the library, then the code should print an error.
				// If the book exists in the library, this code should print the ISBN, number of copies in the library, and the number of copies availabvle
			} else if (line.startsWith("return")) {
				// TODO: Implement this case.
				// Format of the line is
				// return <isbn>
				// e.g. return ISBN-1234
				// NOTE: If the book was never checked out, this code should print an error.
			} else if (line.startsWith("list")) {
				// TODO: Implement this case.
				// Format of the line is 
				// list <isnb>
				// e.g. list ISBN-1234
				// NOTE: This code should print out the number of copies in the library and the number of copies available.
			} else if (line.startsWith("save")) {
				// TODO: Implement this case.
				// Format of the line is
				// save <filename>
				// e.g. save LbraryFile.dat
			} else if (line.startsWith("load")) {
				// TODO: Implement this case.
				// Format of the line is:
				// load <filename>
				// e.g. load LibraryFile.dat
			} else if (line.startsWith("exit")) {
				break;
			}
		}
	}
}
