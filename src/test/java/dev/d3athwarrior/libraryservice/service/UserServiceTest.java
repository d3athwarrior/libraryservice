package dev.d3athwarrior.libraryservice.service;

import dev.d3athwarrior.libraryservice.entity.Book;
import dev.d3athwarrior.libraryservice.entity.Issue;
import dev.d3athwarrior.libraryservice.repository.IssueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IssueRepository issueRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(issueRepository);
    }

    @Test
    void givenUserHasBeenIssuedTwoBooks_whenUserReturnsOneOfTheBook_thenFreeOneCopyOfThatBook_AndReturnResultMapWithDetails() {
        List<Issue> userIssuedBooks = new ArrayList<>();
        userIssuedBooks.add(new Issue());
        given(issueRepository.deleteByBook_IdAndUser_Id(anyLong(), anyLong())).willReturn(1);
        given(issueRepository.findIssuesByUser_Id(anyLong())).willReturn(userIssuedBooks);
        Map<String, Object> result = userService.issuedBookReturned(1L, 1L);
        assertThat(result).isNotNull();
        assertThat(result.get("userId")).isEqualTo(1L);
        assertThat(result.get("returnedBookId")).isEqualTo(1L);
        assertThat(result.get("message")).isEqualTo("Book returned successfully");
        assertThat((List<Book>) result.get("userBookList")).hasSize(1);
    }

    @Test
    void givenUserHasBeenIssuedOnlyOneCopy_whenUserReturnsOneBook_thenFreeOneCopyOfThatBook_AndReturnResultMapWithNoBookForUser() {
        List<Issue> userIssuedBooks = new ArrayList<>();
        given(issueRepository.deleteByBook_IdAndUser_Id(anyLong(), anyLong())).willReturn(1);
        given(issueRepository.findIssuesByUser_Id(anyLong())).willReturn(userIssuedBooks);
        Map<String, Object> result = userService.issuedBookReturned(1L, 1L);
        assertThat(result).isNotNull();
        assertThat(result.get("userId")).isEqualTo(1L);
        assertThat(result.get("returnedBookId")).isEqualTo(1L);
        assertThat(result.get("message")).isEqualTo("Book returned successfully");
        assertThat((List<Book>) result.get("userBookList")).hasSize(0);
    }

    @Test
    void givenUserHasNotBeenIssuedTheBook_whenUserTriesToReturnTheBook_thenErrorMessageIsReturned() {
        given(issueRepository.deleteByBook_IdAndUser_Id(anyLong(), anyLong())).willReturn(0);
        Map<String, Object> result = userService.issuedBookReturned(1L, 1L);
        assertThat(result).isNotNull();
        assertThat(result.get("userId")).isNull();
        assertThat(result.get("returnedBookId")).isNull();
        assertThat(result.get("message")).isEqualTo("You were not issued this book");
        assertThat((Boolean) result.get("hasError")).isEqualTo(true);
    }
}