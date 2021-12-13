package dev.d3athwarrior.libraryservice.controller;

import dev.d3athwarrior.libraryservice.entity.Book;
import dev.d3athwarrior.libraryservice.entity.Issue;
import dev.d3athwarrior.libraryservice.entity.User;
import dev.d3athwarrior.libraryservice.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void givenLibraryHasTwoBooks_whenGetRequestToBooksAllEndpoint_thenTwoBooksAreReturned() throws Exception {
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1L, "The Alchemist", "Paulo Coelho", 2));
        Book b = new Book();
        b.setId(2L);
        b.setName("The Power of Subconscious Mind");
        b.setAuthorName("Dr Joesph Murphy");
        b.setNumOfCopies(1);
        bookList.add(b);
        given(bookService.getAllBooks()).willReturn(bookList);
        given(bookService.getRemainingBookCount(anyLong())).willReturn(1, 0);
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("The Alchemist", "The Power of Subconscious Mind")))
                .andExpect(jsonPath("$[*].authorName", containsInAnyOrder("Paulo Coelho", "Dr Joesph Murphy")))
                .andExpect(jsonPath("$[*].numOfCopies", containsInAnyOrder(2, 1)))
                .andExpect(jsonPath("$[*].numCopiesAvailable", containsInAnyOrder(1, 0)))
                .andReturn();
        verify(bookService, times(1)).getAllBooks();
        verify(bookService, times(2)).getRemainingBookCount(anyLong());
    }

    @Test
    void givenNoBooksInLibrary_whenGetRequestToBooksAll_thenNoBooksAreReturned() throws Exception {
        given(bookService.getAllBooks()).willReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(jsonPath("$[*]", empty()))
                .andReturn();
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void givenBooksInLibrary_whenUserBorrowsABook_thenBookRemovedFromLibrary() throws Exception {
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setBook(new Book(1L, "The Alchemist", "Paulo Coelho", 1));
        issue.setUser(new User(1L, "Test", "User"));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("issue", issue);
        resultMap.put("hasError", false);
        resultMap.put("message", "Book issued successfully");
        given(bookService.issueBook(anyLong(), anyLong())).willReturn(resultMap);
        given(bookService.getRemainingBookCount(anyLong())).willReturn(0);
        mockMvc.perform(MockMvcRequestBuilders.post("/books/1/issueto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(Map.class)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.bookDTO", isA(Map.class)))
                .andExpect(jsonPath("$.bookDTO.id", is(1)))
                .andExpect(jsonPath("$.bookDTO.numCopiesAvailable", is(0)))
                .andExpect(jsonPath("$.hasError", is(false)))
                .andExpect(jsonPath("$.message", is("Book issued successfully")))
                .andReturn();

    }

    @Test
    void givenBooksInLibrary_whenUserBorrowsABook_andUserAlreadyHasBorrowedTwoBooks_thenUserSeesAFailureMessage() throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("issue", null);
        result.put("hasError", true);
        result.put("message", "You have borrowed maximum number of books allowed");
        given(bookService.issueBook(anyLong(), anyLong())).willReturn(result);
        mockMvc.perform(MockMvcRequestBuilders.post("/books/1/issueto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(Map.class)))
                .andExpect(jsonPath("$.userId", nullValue()))
                .andExpect(jsonPath("$.bookDTO", nullValue()))
                .andExpect(jsonPath("$.hasError", is(true)))
                .andExpect(jsonPath("$.message", is("You have borrowed maximum number of books allowed")))
                .andReturn();

    }

    @Test
    void givenBooksInLibraryWithMoreThanOneCopy_whenUserBorrowsOneCopy_thenBookOnlyOneCopyBecomesUnavailable() throws Exception {
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setBook(new Book(1L, "The Alchemist", "Paulo Coelho", 2));
        issue.setUser(new User(1L, "Test", "User"));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("issue", issue);
        resultMap.put("hasError", false);
        resultMap.put("message", "Book issued successfully");
        given(bookService.issueBook(anyLong(), anyLong())).willReturn(resultMap);
        given(bookService.getRemainingBookCount(anyLong())).willReturn(1);
        mockMvc.perform(MockMvcRequestBuilders.post("/books/1/issueto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(Map.class)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.bookDTO", isA(Map.class)))
                .andExpect(jsonPath("$.bookDTO.id", is(1)))
                .andExpect(jsonPath("$.bookDTO.numCopiesAvailable", is(1)))
                .andExpect(jsonPath("$.hasError", is(false)))
                .andExpect(jsonPath("$.message", is("Book issued successfully")))
                .andReturn();
    }

    @Test
    void givenLibraryHasMultipleCopiesOfABook_andUserHasAlreadyBorrowedOneCopy_whenUserRequestsAnotherCopy_thenErrorMessageIsReturned() throws Exception {
        Map<String, Object> issueResult = new HashMap<>();
        issueResult.put("message", "You have already been issued one copy of this book");
        issueResult.put("hasError", true);
        issueResult.put("issue", null);
        given(bookService.issueBook(anyLong(), anyLong())).willReturn(issueResult);
        given(bookService.getRemainingBookCount(anyLong())).willReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/books/1/issueto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(Map.class)))
                .andExpect(jsonPath("$.hasError", is(true)))
                .andExpect(jsonPath("$.message", is("You have already been issued one copy of this book")))
                .andReturn();
    }

//    POST: books -> service -> id

//    response: 201, header location: books/id

    @Test
    void givenUserIsAnAdmin_whenThisUserAddsABookToTheLibrary_thenTheNewlyAddedBookShouldBeVisible() throws Exception {
        given(bookService.addBook(new Book("New Book", "Random", 5))).willReturn(1);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/books")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{" +
                                        "\"authorName\":\"Random\", " +
                                        "\"numOfCopies\":5, " +
                                        "\"name\":\"New Book\"" +
                                        "}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andExpect(header().string("location", "/books/1"))
                .andReturn();
//                .andExpect();
        verify(bookService, times(1)).addBook(new Book());
    }
}

