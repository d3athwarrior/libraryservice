package dev.d3athwarrior.libraryservice.service;

import dev.d3athwarrior.libraryservice.entity.Book;
import dev.d3athwarrior.libraryservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    private BookService bookService;

    @BeforeEach
    public void setUp() {
        bookService = new BookService(bookRepository);
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
}