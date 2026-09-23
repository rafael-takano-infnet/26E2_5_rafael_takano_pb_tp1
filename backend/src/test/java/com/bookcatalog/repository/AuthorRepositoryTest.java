package com.bookcatalog.repository;

import com.bookcatalog.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void savesAndFindsAuthor() {
        Author author = new Author("Machado de Assis", "Escritor brasileiro");

        Author saved = authorRepository.save(author);

        assertThat(saved.getId()).isNotNull();
        assertThat(authorRepository.findById(saved.getId())).isPresent();
    }
}
