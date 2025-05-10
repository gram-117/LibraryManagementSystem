import java.util.Scanner;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
/**
 * A library management class. Has a simple shell that users can interact with to add/remove/checkout/list books in the library.
 * Also allows saving the library state to a file and reloading it from the file.
 */
public class Library {
    /**
     * books are stored in database using ISBN 
     * the ISBN can internally found using author and title
     */
    //private HashMap<String, Book> bookDatabase;
    private HashMap<String, ArrayList<Book>> bookDatabase;
    private HashMap<String, String> ISBNlookup;
    /**
     * Adds a book to the library. If the library already has this book then it
     * adds the number of copies the library has.
     */
    public void addBook(Book book) {
        // create new book[] if new book and add into ISBN loopup the 
        String titleAuthor = book.getTitle() + "|" + book.getAuthor();
        String isbn = book.getIsbn();
        if (bookDatabase.get(isbn) == null) {
            ISBNlookup.put(titleAuthor, isbn);
            ArrayList<Book> bookArr = new ArrayList();
            bookArr.add(book);
            bookDatabase.put(isbn, bookArr); // set book copies to 1 somewhere else?
        } else {
            (bookDatabase.get(isbn)).add(book);
            book.addCopies(bookDatabase.get(isbn).size()); // size of Arraylist = num of books? maybe - 1?
            for (Book oneBook: bookDatabase.get(isbn)) {
                //iterate and update size? unless able to be handled somewhere else
            }
            //have a new arraylist for each release year? 
        }
        //easier but ignores different publication dates 
        // String titleAuthor = book.getTitle() + "|" + book.getAuthor();
        // String isbn = book.getIsbn();
        // ISBNlookup.put(titleAuthor, isbn);
        // if (bookDatabase.get(isbn) == null) {
        //     bookDatabase.put(isbn, book);
        // } else {
        //     (bookDatabase.get(isbn)).addCopies(1);
        // }            
    }

    /**
     * Checks out the given book from the library. Throw the appropriate
     * exception if book doesnt exist or there are no more copies available.
     */
    public void checkout(String isbn) {
        // TODO: Implement this method.
        // look up book if it DNE add it other wise
        // increase the number of copies 
        // maybe add a checked out private method to book class, iterate through arraylist until you 
        // find not checked out book, else throw error
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Returns a book to the library
     */
    public void returnBook(String isnb) {
        // TODO: Implement this method.
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Finds this book in the library. Throws appropriate exception if the book
     * doesnt exist.
     */
    public Book findByTitleAndAuthor(String title, String author) {
        String titleAndAuthor = title + '|' + author;
        String isbn = ISBNlookup.get(titleAndAuthor);
        Book book = bookDatabase.get(isbn);
        if (book == null) {
            // throw ex "not found"
            // maybe check if copies == 0? unless this is handled somewhere else
        }
        return book;
    }

    /**
     * Finds this book in the library. Throws appropriate exception if the book
     * doesnt exist.
     */
    public Book findByISBN(String isbn) {
        Book book = bookDatabase.get(isbn);
        if (book == null) {
            //throw ex "not found"
        }
        return book;
    }

    /**
     * Saves the contents of this library to the given file.
     */
    public void save(String filename) { 
        File outFile = new File(filename);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            Set<Map.Entry<String, ArrayList<Book>>> bookSet = bookDatabase.entrySet();
            for (Map.Entry<String, ArrayList<Book>> item : bookSet) {
                ArrayList<Book> bookArr = item.getValue();
                for (Book book : bookArr) {
                    //add a new formatted line for each book, containg all of the necessary data:
                    //title, author, checked in/out, publish date, isbn
                }
            }
        }
        catch (Exception ex) { // change to relevant exception
            //throw whatever
        }
    }

    /**
     * Loads the contents of this library from the given file. All existing data
     * in this library is cleared before loading from the file.
     */
    public void load(String filename) {
        File inFile = new File(filename);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inFile));
            String line; 
            while (((line = reader.readLine()) != null)) {
                //each line has data for a book w/ specific formatting
                // reading in all the data and then 
                //add book to hashmap
                //add isbn & author|title pair to secondary hashmap
            }
        }
        catch (Exception ex) { // change to relevant exception
            //throw whatever
        }
    }

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
