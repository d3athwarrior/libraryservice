package dev.d3athwarrior.libraryservice;

import dev.d3athwarrior.libraryservice.dto.BookDTO;
import dev.d3athwarrior.libraryservice.dto.BookIssueDTO;
import dev.d3athwarrior.libraryservice.dto.UserBookResponseDTO;
import dev.d3athwarrior.libraryservice.dto.UserReturnBookResponseDTO;
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

import java.util.Arrays;

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
        ResponseEntity<BookDTO[]> listResponseEntity = testRestTemplate.getForEntity("/books", BookDTO[].class);
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
        ResponseEntity<BookDTO[]> listResponseEntity = testRestTemplate.getForEntity("/books", BookDTO[].class);
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
        ResponseEntity<UserBookResponseDTO> userBookResponseDTOResponseEntity = testRestTemplate.getForEntity("/users/" + u1.getId() + "/issuedBooks/",
                UserBookResponseDTO.class);
        assertEquals(200, userBookResponseDTOResponseEntity.getStatusCodeValue());
        UserBookResponseDTO dto1 = userBookResponseDTOResponseEntity.getBody();
        assertNotNull(dto1);
        assertEquals(u1.getId(), dto1.getUserId());
        assertEquals(b2.getId(), dto1.getIssuedBooks().get(0).getId());

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
        ResponseEntity<UserBookResponseDTO> userBookResponseDTOResponseEntity = testRestTemplate.getForEntity("/users/" + u1.getId() + "/issuedBooks/",
                UserBookResponseDTO.class);
        assertEquals(200, userBookResponseDTOResponseEntity.getStatusCodeValue());
        UserBookResponseDTO dto1 = userBookResponseDTOResponseEntity.getBody();
        assertNotNull(dto1);
        assertEquals(u1.getId(), dto1.getUserId());
        assertEquals(b1.getId(), dto1.getIssuedBooks().get(0).getId());
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
        ResponseEntity<BookIssueDTO> bookIssueDTOResponseEntity = testRestTemplate.postForEntity("/books/" + b1.getId() + "/issueto/" + u1.getId(),
                null,
                BookIssueDTO.class);
        BookIssueDTO dto = bookIssueDTOResponseEntity.getBody();
        assertNotNull(dto);
        assertFalse(dto.getHasError());
        assertEquals("Book issued successfully", dto.getMessage());
        assertEquals(b1.getId(), dto.getBookDTO().getId());
        assertEquals(0, dto.getBookDTO().getNumCopiesAvailable());
        ResponseEntity<UserBookResponseDTO> userBookResponseDTOResponseEntity = testRestTemplate.getForEntity("/users/" + u1.getId() + "/issuedBooks/",
                UserBookResponseDTO.class);
        assertEquals(200, userBookResponseDTOResponseEntity.getStatusCodeValue());
        UserBookResponseDTO dto1 = userBookResponseDTOResponseEntity.getBody();
        assertNotNull(dto1);
        assertEquals(u1.getId(), dto1.getUserId());
        assertEquals(b1.getId(), dto1.getIssuedBooks().get(0).getId());
    }
    // END: User story 3

    // START: User story 4

    /**
     * User can return books to the library
     * <p>
     * Given, I have 2 books in my borrowed list
     * When, I return one book to the library
     * Then, the book is removed from my borrowed list
     * And, the library reflects the updated stock of the book
     */
    @Test
    void givenUserHasBorrowedTwoBooks_whenUserReturnsOneBook_thenTheBookShouldBeRemovedFromUserIssuedBooks_andLibraryWillReflectTheUpdatedNumberOfCopiesAvailable() {
        Book b1 = bookRepository.saveAndFlush(new Book(null, "TestName", "TestAuthorName", 1));
        Book b2 = bookRepository.saveAndFlush(new Book(null, "TestName2", "TestAuthorName2", 1));
        User u1 = userRepository.saveAndFlush(new User(null, "Test", "User"));
        issueRepository.saveAndFlush(new Issue(null, u1, b1));
        issueRepository.saveAndFlush(new Issue(null, u1, b2));
        ResponseEntity<UserReturnBookResponseDTO> userBookDTOResponseEntity = testRestTemplate.postForEntity("/users/" + u1.getId() + "/returnbook/" + b1.getId(),
                null,
                UserReturnBookResponseDTO.class);
        UserReturnBookResponseDTO dto = userBookDTOResponseEntity.getBody();
        assertNotNull(dto);
        assertEquals(u1.getId(), dto.getUserId());
        assertEquals(b1.getId(), dto.getReturnedBookId());
        assertEquals(1, dto.getIssuedBooks().size());
        assertEquals(b2.getName(), dto.getIssuedBooks().get(0).getName());
        ResponseEntity<BookDTO[]> responseEntity = testRestTemplate.getForEntity("/books", BookDTO[].class);
        assertNotNull(responseEntity.getBody());
        assertTrue(Arrays.stream(responseEntity.getBody())
                .anyMatch(bookDTO -> bookDTO.getName().equals(b1.getName()) && bookDTO.getNumCopiesAvailable().equals(b1.getNumOfCopies())));
        ResponseEntity<UserBookResponseDTO> userBookResponseDTOResponseEntity = testRestTemplate.getForEntity("/users/" + u1.getId() + "/issuedBooks/",
                UserBookResponseDTO.class);
        assertEquals(200, userBookResponseDTOResponseEntity.getStatusCodeValue());
        UserBookResponseDTO dto1 = userBookResponseDTOResponseEntity.getBody();
        assertNotNull(dto1);
        assertEquals(u1.getId(), dto1.getUserId());
        assertEquals(b2.getId(), dto1.getIssuedBooks().get(0).getId());

    }

    /**
     * Given, I have 2 books in my borrowed list
     * When, I return both books to the library
     * Then, my borrowed list is empty
     * And, the library reflects the updated stock of the books
     */
    @Test
    void givenUserHasBorrowedTwoBooks_whenUserReturnsBothTheBooks_thenTheIssuedBooksToUserAreEmpty_andTheLibraryReflectsOriginalAvailableQuantities() {
        Book b1 = bookRepository.saveAndFlush(new Book(null, "TestName", "TestAuthorName", 1));
        Book b2 = bookRepository.saveAndFlush(new Book(null, "TestName2", "TestAuthorName2", 1));
        User u1 = userRepository.saveAndFlush(new User(null, "Test", "User"));
        issueRepository.saveAndFlush(new Issue(null, u1, b1));
        issueRepository.saveAndFlush(new Issue(null, u1, b2));
        ResponseEntity<UserReturnBookResponseDTO> userBookDTOResponseEntity = testRestTemplate.postForEntity("/users/" + u1.getId() + "/returnbook/" + b1.getId(),
                null,
                UserReturnBookResponseDTO.class);
        UserReturnBookResponseDTO dto = userBookDTOResponseEntity.getBody();
        assertNotNull(dto);
        assertEquals(1, dto.getIssuedBooks().size());
        ResponseEntity<UserReturnBookResponseDTO> userBookDTOResponseEntity1 = testRestTemplate.postForEntity("/users/" + u1.getId() + "/returnbook/" + b2.getId(),
                null,
                UserReturnBookResponseDTO.class);
        UserReturnBookResponseDTO dto1 = userBookDTOResponseEntity1.getBody();
        assertNotNull(dto1);
        assertEquals(0, dto1.getIssuedBooks().size());
        ResponseEntity<BookDTO[]> responseEntity = testRestTemplate.getForEntity("/books", BookDTO[].class);
        assertNotNull(responseEntity.getBody());
        assertTrue(Arrays.stream(responseEntity.getBody())
                .anyMatch(bookDTO -> bookDTO.getName().equals(b1.getName()) && bookDTO.getNumCopiesAvailable().equals(b1.getNumOfCopies())));
        assertTrue(Arrays.stream(responseEntity.getBody())
                .anyMatch(bookDTO -> bookDTO.getName().equals(b2.getName()) && bookDTO.getNumCopiesAvailable().equals(b1.getNumOfCopies())));
        ResponseEntity<UserBookResponseDTO> userBookResponseDTOResponseEntity = testRestTemplate.getForEntity("/users/" + u1.getId() + "/issuedBooks/",
                UserBookResponseDTO.class);
        assertEquals(200, userBookResponseDTOResponseEntity.getStatusCodeValue());
        UserBookResponseDTO dto2 = userBookResponseDTOResponseEntity.getBody();
        assertNotNull(dto2);
        assertEquals(u1.getId(), dto1.getUserId());
        assertEquals(0, dto1.getIssuedBooks().size());
    }
    // END: USer story 4

    // START: User Login Integration Test
    @Test
    void givenUsersInApplication_whenLoginForValidUser_thenUserIdShouldBeReturned() {
        User u1 = userRepository.saveAndFlush(new User(null, "Test", "User"));
        ResponseEntity<Long> userBookDTOResponseEntity = testRestTemplate.postForEntity("/login",
                u1.getId(),
                Long.class);
        Long responseId = userBookDTOResponseEntity.getBody();
        assertNotNull(responseId);
        assertEquals(u1.getId(), responseId);
    }

    @Test
    void givenUsersInApplication_whenLoginForInvalidUser_thenUserIdShouldBeNegative() {
        User u1 = userRepository.saveAndFlush(new User(null, "Test", "User"));
        ResponseEntity<Long> userBookDTOResponseEntity = testRestTemplate.postForEntity("/login",
                10000L,
                Long.class);
        Long responseId = userBookDTOResponseEntity.getBody();
        assertNotNull(responseId);
        assertEquals(-1L, responseId);
    }
    // END: User login integration test

    @AfterEach
    public void tearDown() {
        issueRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
    }
}
