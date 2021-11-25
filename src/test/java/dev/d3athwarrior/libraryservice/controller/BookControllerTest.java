package dev.d3athwarrior.libraryservice.controller;

import dev.d3athwarrior.libraryservice.entity.Book;
import dev.d3athwarrior.libraryservice.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        mockMvc.perform(MockMvcRequestBuilders.get("/books/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("The Alchemist", "The Power of Subconscious Mind")))
                .andExpect(jsonPath("$[*].authorName", containsInAnyOrder("Paulo Coelho", "Dr Joesph Murphy")))
                .andExpect(jsonPath("$[*].numOfCopies", containsInAnyOrder(2, 1)))
//                .andExpect(jsonPath("$[*].numCopiesAvailable", containsInAnyOrder(1, 0))) Uncomment later when we add number of copies to the repository
                .andReturn();
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void givenNoBooksInLibrary_whenGetRequestToBooksAll_thenNoBooksAreReturned() throws Exception {
        given(bookService.getAllBooks()).willReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/books/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(jsonPath("$[*]", empty()))
                .andReturn();
        verify(bookService, times(1)).getAllBooks();
    }
}

