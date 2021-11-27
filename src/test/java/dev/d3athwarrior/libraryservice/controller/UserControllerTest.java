package dev.d3athwarrior.libraryservice.controller;

import dev.d3athwarrior.libraryservice.entity.Book;
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
        List<Book> remainingIssuedBooks = new ArrayList<>();
        remainingIssuedBooks.add(new Book());
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
                .andExpect(jsonPath("$.userBookList.[*]", hasSize(1)))
                .andReturn();
    }

    @Test
    void givenUserHasTwoBooksWithThem_whenUserReturnsBothTheBooks_thenUserBookDTOSHouldReturnEmptyUserBookList() throws Exception {
        List<Book> remainingIssuedBooks = new ArrayList<>();
        remainingIssuedBooks.add(new Book());
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
                .andExpect(jsonPath("$.userBookList.[*]", hasSize(1)))
                .andReturn();

        remainingIssuedBooks.clear();
        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/returnbook/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(Map.class)))
                .andExpect(jsonPath("$.returnedBookId", is(1)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.message", is("Book returned successfully")))
                .andExpect(jsonPath("$.userBookList.[*]", hasSize(0)))
                .andReturn();
        verify(userService, times(2)).issuedBookReturned(anyLong(), anyLong());
    }
}
