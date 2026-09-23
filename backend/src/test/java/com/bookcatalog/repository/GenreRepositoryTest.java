package com.bookcatalog.repository;

import com.bookcatalog.domain.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void savesAndFindsGenre() {
        Genre genre = new Genre("Ficção", "Livros de ficção");

        Genre saved = genreRepository.save(genre);

        assertThat(saved.getId()).isNotNull();
        assertThat(genreRepository.findById(saved.getId())).isPresent();
    }
}
