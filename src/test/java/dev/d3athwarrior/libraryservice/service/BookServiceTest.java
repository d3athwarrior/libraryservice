package dev.d3athwarrior.libraryservice.service;

import dev.d3athwarrior.libraryservice.entity.Book;
import dev.d3athwarrior.libraryservice.entity.Issue;
import dev.d3athwarrior.libraryservice.entity.User;
import dev.d3athwarrior.libraryservice.repository.BookRepository;
import dev.d3athwarrior.libraryservice.repository.IssueRepository;
import dev.d3athwarrior.libraryservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IssueRepository issueRepository;

    private BookService bookService;

    @BeforeEach
    public void setUp() {
        bookService = new BookService(bookRepository, issueRepository, userRepository);
    }

    @Test
    void givenNoBooksInLibrary_whenAllBooksRequested_thenEmptyListIsReturned() {
        given(bookRepository.findAll()).willReturn(new ArrayList<>());
        assertThat(bookService.getAllBooks()).hasSize(0);
    }

    @Test
    void givenTwoBooksInLibrary_whenAllBooksRequested_thenTwoBooksAreReturned() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1L, "The Alchemist", "Paulo Coelho", 1));
        Book b = new Book();
        b.setId(2L);
        b.setName("How to tame your mind");
        b.setAuthorName("");
        b.setNumOfCopies(5);
        bookList.add(b);
        given(bookRepository.findAll()).willReturn(bookList);
        assertThat(bookService.getAllBooks()).hasSize(2);
    }

    @Test
    void givenBooksInLibrary_whenUserRequestsBookIssue_thenBookIsIssued() {
        Issue issueToReturn = new Issue();
        issueToReturn.setId(1L);
        issueToReturn.setBook(new Book(1L, null, null, null));
        issueToReturn.setUser(new User(1L, null, null));
        given(issueRepository.countByUser_Id(anyLong())).willReturn(1);
        given(issueRepository.save(any(Issue.class))).willReturn(issueToReturn);
        Optional<Issue> issueReturnHolder = bookService.issueBook(1, 1);
        assertThat(issueReturnHolder.get().getId()).isEqualTo(1L);
        assertThat(issueReturnHolder.get().getBook()).isNotNull();
        assertThat(issueReturnHolder.get().getUser()).isNotNull();
        assertThat(issueReturnHolder.get().getBook().getId()).isEqualTo(1L);
        assertThat(issueReturnHolder.get().getUser().getId()).isEqualTo(1L);
    }

    @Test
    void givenBooksInLibrary_whenUserRequestsBookIssue_andUserAlreadyIsIssuedTwoBooks_thenNullIssueIsReturned() {
        given(issueRepository.countByUser_Id(anyLong())).willReturn(2);
        assertThat(bookService.issueBook(1, 1).isEmpty()).isTrue();
    }

    @Test
    void givenBooksInLibrary_whenRemainingCountOfABookRequested_thenReturnTheRemainingCount() {
        given(bookRepository.findById(anyLong())).willReturn(Optional.of(new Book(1L, "Dummy Book", "Dummy Author", 2)));
        given(issueRepository.countByBook_Id(anyLong())).willReturn(Optional.of(1));
        assertThat(bookService.getRemainingBookCount(1)).isEqualTo(1);
    }

    @Test
    void givenBookNotInLibrary_whenRemainingCountOfTheBookRequested_thenReturnZero() {
        given(bookRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThat(bookService.getRemainingBookCount(1)).isEqualTo(0);
    }
}