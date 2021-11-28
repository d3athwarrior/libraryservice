package dev.d3athwarrior.libraryservice.controller;

import dev.d3athwarrior.libraryservice.entity.Book;
import dev.d3athwarrior.libraryservice.entity.Issue;
import dev.d3athwarrior.libraryservice.entity.User;
import dev.d3athwarrior.libraryservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void givenUserHasTwoBooksWithThem_whenUserReturnsOneOfTheBook_thenUserBookDTOShouldReturnOneBook() throws Exception {
        List<Issue> remainingIssuedBooks = new ArrayList<>();
        remainingIssuedBooks.add(new Issue(null, new User(), new Book(1L, "TestBook", "Test Author", 1)));
        Map<String, Object> bookReturnResult = new HashMap<>();
        bookReturnResult.put("message", "Book returned successfully");
        bookReturnResult.put("userBookList", remainingIssuedBooks);
        bookReturnResult.put("returnedBookId", 1L);
        bookReturnResult.put("userId", 1L);
        given(userService.issuedBookReturned(anyLong(), anyLong())).willReturn(bookReturnResult);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/returnbook/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(Map.class)))
                .andExpect(jsonPath("$.returnedBookId", is(1)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.message", is("Book returned successfully")))
                .andExpect(jsonPath("$.issuedBooks.[*]", hasSize(1)))
                .andReturn();
    }

    @Test
    void givenUserHasTwoBooksWithThem_whenUserReturnsBothTheBooks_thenUserBookDTOSHouldReturnEmptyUserBookList() throws Exception {
        List<Issue> remainingIssuedBooks = new ArrayList<>();
        remainingIssuedBooks.add(new Issue(null, new User(), new Book(1L, "TestBook", "Test Author", 1)));
        Map<String, Object> bookReturnResult = new HashMap<>();
        bookReturnResult.put("message", "Book returned successfully");
        bookReturnResult.put("userBookList", remainingIssuedBooks);
        bookReturnResult.put("returnedBookId", 1L);
        bookReturnResult.put("userId", 1L);
        given(userService.issuedBookReturned(anyLong(), anyLong())).willReturn(bookReturnResult);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/returnbook/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(Map.class)))
                .andExpect(jsonPath("$.returnedBookId", is(1)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.message", is("Book returned successfully")))
                .andExpect(jsonPath("$.issuedBooks.[*]", hasSize(1)))
                .andReturn();

        remainingIssuedBooks.clear();
        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/returnbook/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(Map.class)))
                .andExpect(jsonPath("$.returnedBookId", is(1)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.message", is("Book returned successfully")))
                .andExpect(jsonPath("$.issuedBooks.[*]", hasSize(0)))
                .andReturn();
        verify(userService, times(2)).issuedBookReturned(anyLong(), anyLong());
    }

    @Test
    void givenUserHasBorrowedBooks_whenARequestForUserBooks_thenReturnTheListOfBooksForThatUser() throws Exception {
        List<Issue> issuedBookList = new ArrayList<>();
        User user = new User(1L, "Test", "User");
        issuedBookList.add(new Issue(1L,
                user,
                new Book(1L, "Test Book", "Test Author", 1)));
        issuedBookList.add(new Issue(2L,
                user,
                new Book(2L, "Test Book", "Test Author", 1)));
        given(userService.getUserBooks(anyLong())).willReturn(issuedBookList);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/issuedBooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(Map.class)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.issuedBooks[*]", hasSize(2)))
                .andReturn();
    }

    @Test
    void givenUserHasNoBorrowedBooks_whenARequestForUserBooks_thenReturnEmpty() throws Exception {
        given(userService.getUserBooks(anyLong())).willReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/issuedBooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(Map.class)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.issuedBooks[*]", hasSize(0)))
                .andReturn();
    }
}
