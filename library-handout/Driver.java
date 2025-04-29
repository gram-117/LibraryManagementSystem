
public class Driver extends DriverBase {

    private static final String FILENAME = "Library.out";

    private static void checkLibrary(Library library, Book ... books) {
        for (Book book : books) {
            Book b = library.findByISBN(book.getIsbn());
            if (b == null) {
                setTestResult("Book " + book.getIsbn() + " not found in library.");
                return;
            } else if (!b.equals(book)) {
                setTestResult("Book " + b.getIsbn() + " not equal to expected book " + book.getIsbn());
                return;
            }
            b = library.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
            if (b == null) {
                setTestResult("Book " + book.getIsbn() + " not found in library.");
                return;
            } else if (!b.equals(book)) {
                setTestResult("Book " + b.getIsbn() + " not equal to expected book " + book.getIsbn());
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        // All checks should be done both by ISBN and TitleAndAuthor
        // Test adding a book and finding 
        setCurrentTestCase("Checking one book in library");
        checkDoesNotThrowException(() -> {
            Library library = new Library();
            Book b1 = new Book("Foo", "Author", "1234", 2024, 10);
            library.addBook(b1);
            checkLibrary(library, b1);
        });
        // Test adding the same book twice and checking number of copies available
        setCurrentTestCase("Checking same book added twice in library");
        checkDoesNotThrowException(() -> {
            Library library = new Library();
            Book b1 = new Book("Foo", "Author", "1234", 2024, 10);
            library.addBook(b1);
            library.addBook(b1);
            // We expect twice the copies.
            Book b2 = new Book("Foo", "Author", "1234", 2024, 20);
            checkLibrary(library, b2);
        });
        // Test adding multiple books and finding the first one
        setCurrentTestCase("Checking multiple books added to library");
        checkDoesNotThrowException(() -> {
            Library library = new Library();
            Book b1 = new Book("Foo", "Author", "1234", 2024, 10);
            Book b2 = new Book("Foo2", "Author2", "1236", 2024, 10);
            library.addBook(b1);
            library.addBook(b2);
            // We expect twice the copies.
            checkLibrary(library, b1, b2);
        });
        // Test adding multiple books and checking out one and checking book
        setCurrentTestCase("Checking checking out books added to library");
        checkDoesNotThrowException(() -> {
            Library library = new Library();
            Book b1 = new Book("Foo", "Author", "1234", 2024, 10);
            Book b2 = new Book("Foo2", "Author2", "1236", 2024, 10);
            library.addBook(b1);
            library.addBook(b2);
            library.checkout("1234");
            
            Book b1_new = new Book("Foo", "Author", "1234", 2024, 9);

            // We expect twice the copies.
            checkLibrary(library, b1_new, b2);
        });
        // Test adding multiple books with the same title but different author
        setCurrentTestCase("Adding multiple books with same title");
        checkDoesNotThrowException(() -> {
            Library library = new Library();
            Book b1 = new Book("Foo", "Author", "1234", 2024, 10);
            Book b2 = new Book("Foo", "Author2", "1236", 2024, 10);
            library.addBook(b1);
            library.addBook(b2);
            // We expect twice the copies.
            checkLibrary(library, b1, b2);
        });
        // Test adding a book and returning the book to the library (should fail)
        setCurrentTestCase("Returning a book that hasnt been checked out. Should throw exception.");
        checkThrowsException(() -> {
            Library library = new Library();
            Book b1 = new Book("Foo", "Author", "1234", 2024, 10);
            library.addBook(b1);
            library.returnBook("1234");
        });
        // Test adding a book, checking out, returning and check number of copies
        setCurrentTestCase("Checking out multiple books");
        checkDoesNotThrowException(() -> {
            Library library = new Library();
            Book b1 = new Book("Foo", "Author", "1234", 2024, 10);
            Book b2 = new Book("Foo2", "Author2", "1236", 2024, 10);
            Book b3 = new Book("Foo2", "Author2", "1237", 2024, 10);
            library.addBook(b1);
            library.addBook(b2);
            library.addBook(b3);
            library.checkout("1234");
            library.checkout("1236");
            Book b1_new = new Book("Foo", "Author", "1234", 2024, 9);
            Book b2_new = new Book("Foo2", "Author2", "1236", 2024, 9);
            checkLibrary(library, b1_new, b2_new, b3);
        });
        // Test adding multiple books, checking them out and returning some of them
        setCurrentTestCase("Checking out and returning multiple books");
        checkDoesNotThrowException(() -> {
            Library library = new Library();
            Book b1 = new Book("Foo", "Author", "1234", 2024, 10);
            Book b2 = new Book("Foo2", "Author2", "1236", 2024, 10);
            Book b3 = new Book("Foo2", "Author2", "1237", 2024, 10);
            library.addBook(b1);
            library.addBook(b2);
            library.addBook(b3);
            library.checkout("1234");
            library.checkout("1236");
            library.returnBook("1234");
            Book b2_new = new Book("Foo2", "Author2", "1236", 2024, 9);
            checkLibrary(library, b1, b2_new, b3);
        });
        // Test adding books, checking out, saving, restoring, find the books and verify counts
        setCurrentTestCase("Saving and reloading");
        checkDoesNotThrowException(() -> {
            Library library = new Library();
            Book b1 = new Book("Foo", "Author", "1234", 2024, 10);
            Book b2 = new Book("Foo2", "Author2", "1236", 2024, 10);
            Book b3 = new Book("Foo2", "Author2", "1237", 2024, 10);
            library.addBook(b1);
            library.addBook(b2);
            library.addBook(b3);
            library.checkout("1234");
            library.save(FILENAME);
            library.checkout("1236");
            library.returnBook("1234");
            
            // Load from file.
            Library newLib = new Library();
            newLib.load(FILENAME);

            Book b1_new =  new Book("Foo", "Author", "1234", 2024, 9);
            checkLibrary(newLib, b1_new, b2, b3);
        });

        // Print the output
        testOutput.put("Compilation succeeded", null);
        printJsonOutput();
    }

}
