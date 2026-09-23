package com.bookcatalog.controller;

import com.bookcatalog.exception.ResourceNotFoundException;
import com.bookcatalog.service.AuthorService;
import com.bookcatalog.service.BookService;
import com.bookcatalog.service.GenreService;
import com.bookcatalog.service.PublisherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private PublisherService publisherService;

    @Test
    void returnsNotFoundWhenBookDoesNotExist() throws Exception {
        when(bookService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound());
    }
}
