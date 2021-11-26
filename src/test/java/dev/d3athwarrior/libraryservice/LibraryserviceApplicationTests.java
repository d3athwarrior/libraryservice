package dev.d3athwarrior.libraryservice;

import dev.d3athwarrior.libraryservice.dto.BookDTO;
import dev.d3athwarrior.libraryservice.dto.BookIssueDTO;
import dev.d3athwarrior.libraryservice.entity.Book;
import dev.d3athwarrior.libraryservice.entity.Issue;
import dev.d3athwarrior.libraryservice.entity.User;
import dev.d3athwarrior.libraryservice.repository.BookRepository;
import dev.d3athwarrior.libraryservice.repository.IssueRepository;
import dev.d3athwarrior.libraryservice.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LibraryserviceApplicationTests {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    IssueRepository issueRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;

    // START: User Story 1
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
        bookRepository.save(new Book(null, "TestName", "TestAuthorName", 2));
        bookRepository.save(new Book(null, "TestName2", "TestAuthorName2", 2));
        bookRepository.save(new Book(null, "TestName3", "TestAuthorName3", 2));
        ResponseEntity<BookDTO[]> listResponseEntity = testRestTemplate.getForEntity("/books/all", BookDTO[].class);
        assertNotNull(listResponseEntity.getBody());
        assertEquals(3, listResponseEntity.getBody().length);
    }
    // END: user story 1

    // START: User story 2

    /**
     * User can borrow a book from the library
     * <p>
     * Given, there are books in the library
     * When, I choose a book to add to my borrowed list
     * Then, the book is added to my borrowed list
     * And, the book is removed from the library
     * <p>
     * Note:
     * a. Each User has a borrowing limit of 2 books at any point of time
     */
    @Test
    void givenBooksInLibrary_whenBookIssueRequest_andUserIsNotIssuedTwoBooks_thenBookIsIssuedToUser() {
        Book b1 = bookRepository.save(new Book(null, "TestName", "TestAuthorName", 2));
        Book b2 = bookRepository.save(new Book(null, "TestName2", "TestAuthorName2", 1));
        Book b3 = bookRepository.save(new Book(null, "TestName3", "TestAuthorName3", 2));
        User u1 = userRepository.save(new User(null, "Test", "User"));
        ResponseEntity<BookIssueDTO> bookIssueDTOResponseEntity = testRestTemplate.postForEntity("/books/" + b2.getId() + "/issueto/" + u1.getId(), null, BookIssueDTO.class);
        BookIssueDTO dto = bookIssueDTOResponseEntity.getBody();
        assertNotNull(dto);
        assertEquals(b2.getId(), dto.getBookDTO().getId());
        assertEquals(u1.getId(), dto.getUserId());
        assertEquals(0, dto.getBookDTO().getNumCopiesAvailable());
        assertFalse(dto.getHasError());
        assertEquals("Book issued successfully", dto.getMessage());

    }

    @Test
    void givenBooksInLibrary_whenBookIssueRequest_andUserIsIssuedTwoBooks_thenErrorMessageIsReturned() {
        Book b1 = bookRepository.save(new Book(null, "TestName", "TestAuthorName", 2));
        Book b2 = bookRepository.save(new Book(null, "TestName2", "TestAuthorName2", 1));
        Book b3 = bookRepository.save(new Book(null, "TestName3", "TestAuthorName3", 2));
        User u1 = userRepository.save(new User(null, "Test", "User"));
        issueRepository.save(new Issue(null, u1, b1));
        issueRepository.save(new Issue(null, u1, b2));
        ResponseEntity<BookIssueDTO> bookIssueDTOResponseEntity = testRestTemplate.postForEntity("/books/" + b3.getId() + "/issueto/" + u1.getId(), null, BookIssueDTO.class);
        BookIssueDTO dto = bookIssueDTOResponseEntity.getBody();
        assertNotNull(dto);
        assertNotEquals(b2.getId(), dto.getBookDTO().getId());
        assertNotEquals(u1.getId(), dto.getUserId());
        assertNull(dto.getBookDTO());
        assertTrue(dto.getHasError());
        assertEquals("You have borrowed maximum number of books allowed", dto.getMessage());
    }
    // END: User story 2

    // START: User story 3

    /**
     * User can borrow a copy of a book from the library
     * <p>
     * Given, there are more than one copy of a book in the library
     * When, I choose a book to add to my borrowed list
     * Then, one copy of the book is added to my borrowed list
     * And, the library has at least one copy of the book left
     * <p>
     * Note:
     * a. Only 1 copy of a book can be borrowed by a User at any point of time
     */
    @Test
    void givenMoreThanOneCopyOfABook_whenUserBorrowsABook_thenOneCopyIsBorrowedByUser_andAtLeastOneCopyIsLeft() {
        Book b1 = bookRepository.save(new Book(null, "TestName", "TestAuthorName", 2));
        User u1 = userRepository.save(new User(null, "Test", "User"));
        ResponseEntity<BookIssueDTO> bookIssueDTOResponseEntity = testRestTemplate.postForEntity("/books/" + b1.getId() + "/issueto/" + u1.getId(), null, BookIssueDTO.class);
        BookIssueDTO dto = bookIssueDTOResponseEntity.getBody();
        assertNotNull(dto);
        assertFalse(dto.getHasError());
        assertEquals("Book issued successfully", dto.getMessage());
        assertEquals(b1.getId(), dto.getBookDTO().getId());
        assertEquals(1, dto.getBookDTO().getNumCopiesAvailable());
    }

    /**
     * Given, there is only one copy of a book in the library
     * When, I choose a book to add to my borrowed list
     * Then, one copy of the book is added to my borrowed list
     * And, the book is removed from the library
     */
    @Test
    void givenOnlyOneCopyOfBookInLibrary_whenUserBorrowsTheBook_thenNoCopyShouldBeLeftInLibrary() {
        Book b1 = bookRepository.save(new Book(null, "TestName", "TestAuthorName", 1));
        User u1 = userRepository.save(new User(null, "Test", "User"));
        ResponseEntity<BookIssueDTO> bookIssueDTOResponseEntity = testRestTemplate.postForEntity("/books/" + b1.getId() + "/issueto/" + u1.getId(), null, BookIssueDTO.class);
        BookIssueDTO dto = bookIssueDTOResponseEntity.getBody();
        assertNotNull(dto);
        assertFalse(dto.getHasError());
        assertEquals("Book issued successfully", dto.getMessage());
        assertEquals(b1.getId(), dto.getBookDTO().getId());
        assertEquals(0, dto.getBookDTO().getNumCopiesAvailable());
    }
    // END: User story 3

    @AfterEach
    public void tearDown() {
        issueRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
    }
}
