package dev.d3athwarrior.libraryservice.service;

import dev.d3athwarrior.libraryservice.entity.Book;
import dev.d3athwarrior.libraryservice.entity.Issue;
import dev.d3athwarrior.libraryservice.repository.BookRepository;
import dev.d3athwarrior.libraryservice.repository.IssueRepository;
import dev.d3athwarrior.libraryservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;

    @Autowired
    public BookService(BookRepository bookRepository, IssueRepository issueRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.issueRepository = issueRepository;
    }

    /**
     * This method is used to fetch the list of all the books in the library
     *
     * @return the list of books in the library
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * This method performs the task of issued a book to the user provided certain conditions, which are listed below,
     * are met.
     * Conditions:
     * 1. The user must not have been issued two books already
     * 2. The user must not request another copy of a book
     * <p>
     * These validations can or rather must be moved to the UI
     *
     * @param bookId the id of the book being borrowed
     * @param userId the id of the user borrowing the book
     * @return Optional of {@link Issue}. In case the object has been saved successfully, it will contain the Issue object
     * else it will be empty
     */
    /*
     * A point of improvement is to use a Bean as a return type than a Map which will make things clear on the calling
     * method as to what are the items being returned from this method
     */
    public Map<String, Object> issueBook(long bookId, long userId) {
        Issue newIssue = null;
        Map<String, Object> resultMap = new HashMap<>();
        String message;
        boolean error = false;

        if (issueRepository.countByUser_Id(userId) < 2) {
            if (issueRepository.findIssuesByBook_IdAndUser_Id(bookId, userId).isEmpty()) {
                newIssue = new Issue();
                newIssue.setBook(bookRepository.getById(bookId));
                newIssue.setUser(userRepository.getById(userId));
                newIssue = issueRepository.save(newIssue);
                message = "Book issued successfully";
            } else {
                error = true;
                message = "You have already been issued one copy of this book";
            }
        } else {
            error = true;
            message = "You have borrowed maximum number of books allowed";
        }
        resultMap.put("message", message);
        resultMap.put("issue", newIssue);
        resultMap.put("hasError", error);
        return resultMap;
    }

    /**
     * This method fetches the number of books remaining in the library for a book.
     *
     * @param bookId the id of the book of which remaining copies are to be fetched
     * @return the remaining number of copies of the book
     */
    public int getRemainingBookCount(long bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        return optionalBook.map(book -> book.getNumOfCopies() - issueRepository.countByBook_Id(bookId).orElse(0)).orElse(0);
    }
}
