package com.bookcatalog.repository;

import com.bookcatalog.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void savesAndFindsBook() {
        Book book = new Book();
        book.setTitle("Clean Code");
        book.setIsbn("123456");

        Book saved = bookRepository.save(book);

        assertThat(saved.getId()).isNotNull();
        assertThat(bookRepository.findById(saved.getId())).isPresent();
    }
}
