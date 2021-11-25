package dev.d3athwarrior.libraryservice.controller;

import dev.d3athwarrior.libraryservice.dto.BookDTO;
import dev.d3athwarrior.libraryservice.entity.Book;
import dev.d3athwarrior.libraryservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The BookController holds all the endpoints which will perform book related operations
 * For example: list of all books, detail about a book, updating details on a book, etc
 */
@RestController
@RequestMapping("books")
public class BookController {

    private BookService bookService;

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
        return bookList.stream().map(book -> new BookDTO(book.getId(), book.getName(), book.getAuthorName(), book.getNumOfCopies())).collect(Collectors.toList());
    }

//    /**
//     *
//     * @param bookId the id of which details are to be fetched
//     * @return Details of the book corresponding to the ID
//     */
//    @GetMapping("detail/{bookId}")
//    public String getBookDetail(@PathVariable Integer bookId) {
//        return "This will return detail of a single book";
//    }
}
