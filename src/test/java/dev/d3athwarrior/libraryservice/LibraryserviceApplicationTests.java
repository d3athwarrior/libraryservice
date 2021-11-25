package dev.d3athwarrior.libraryservice;

import dev.d3athwarrior.libraryservice.dto.BookDTO;
import dev.d3athwarrior.libraryservice.entity.Book;
import dev.d3athwarrior.libraryservice.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LibraryserviceApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    BookRepository bookRepository;

    /*
     *  User Story:
     * 1. User can view books in library
     *
     * Scenario: As a User I want to see the books present in the library so that I can choose which book to borrow
     *
     * Given, there are no books in the library
     * When, I view the books in the library
     * Then, I see an empty library
     *
     */
    @Test
    void givenNoBooksInLibrary_whenBooksViewed_thenEmptyLibrary() {
        ResponseEntity<BookDTO[]> listResponseEntity = testRestTemplate.getForEntity("/books/all", BookDTO[].class);
        assertNotNull(listResponseEntity.getBody());
        assertEquals(0, listResponseEntity.getBody().length);
    }

    /*
     *
     * Given, there are books in the library
     * When, I view the books in the library
     * Then, I see the list of books in the library
     */
    @Test
    void givenBooksInLibrary_whenBooksViewed_thenListOfBooks() {
        Book b = new Book();
        b.setName("TestName");
        b.setAuthorName("TestAuthorName");
        b.setNumOfCopies(2);
        Book b2 = new Book();
        b2.setName("TestName2");
        b2.setAuthorName("TestAuthorName2");
        b2.setNumOfCopies(2);
        bookRepository.save(b);
        bookRepository.save(b2);
        ResponseEntity<BookDTO[]> listResponseEntity = testRestTemplate.getForEntity("/books/all", BookDTO[].class);
        assertNotNull(listResponseEntity.getBody());
        assertEquals(2, listResponseEntity.getBody().length);
        bookRepository.deleteAll();
    }
    // End user story 1
}
