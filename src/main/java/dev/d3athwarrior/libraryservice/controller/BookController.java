package dev.d3athwarrior.libraryservice.controller;

import dev.d3athwarrior.libraryservice.dto.BookDTO;
import dev.d3athwarrior.libraryservice.dto.BookIssueDTO;
import dev.d3athwarrior.libraryservice.entity.Book;
import dev.d3athwarrior.libraryservice.entity.Issue;
import dev.d3athwarrior.libraryservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The BookController holds all the endpoints which will perform book related operations
 * For example: list of all books, detail about a book, updating details on a book, etc
 */
@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * @return the list of all books in the library
     */
    @GetMapping("all")
    public List<BookDTO> getAllBooks() {
        List<Book> bookList = bookService.getAllBooks();
        return bookList.stream().map(book -> new BookDTO(book.getId(), book.getName(), book.getAuthorName(), book.getNumOfCopies(), bookService.getRemainingBookCount(book.getId()))).collect(Collectors.toList());
    }

    /**
     * This method will assign the book to the user and return either an error message or the {@link BookIssueDTO} with
     * a success message
     *
     * @param bookId The id of the book being borrowed
     * @param userId The id of the user borrowing the book
     * @return the {@link BookIssueDTO} containing details of the saved book with a
     */
    @PostMapping("{bookId}/issueto/{userId}")
    public BookIssueDTO borrowBook(@PathVariable Long bookId, @PathVariable Long userId) {
        Map<String, Object> issueResult = bookService.issueBook(bookId, userId);
        BookIssueDTO bookIssueDTO = new BookIssueDTO();
        if ((boolean) issueResult.get("hasError")) {
            bookIssueDTO.setHasError((Boolean) issueResult.get("hasError"));
            bookIssueDTO.setMessage(issueResult.get("message").toString());
        } else {
            Issue issue = (Issue) issueResult.get("issue");
            Book issuedBook = issue.getBook();
            bookIssueDTO.setUserId(issue.getUser().getId());
            bookIssueDTO.setMessage(issueResult.get("message").toString());
            bookIssueDTO.setBookDTO(new BookDTO(issuedBook.getId(), issuedBook.getName(), issuedBook.getAuthorName(), issuedBook.getNumOfCopies(), bookService.getRemainingBookCount(issuedBook.getId())));
        }
        return bookIssueDTO;
    }
}
