package com.bookcatalog.repository;

import com.bookcatalog.domain.Publisher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PublisherRepositoryTest {

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    void savesAndFindsPublisher() {
        Publisher publisher = new Publisher("Editora Abril", "Brasil");

        Publisher saved = publisherRepository.save(publisher);

        assertThat(saved.getId()).isNotNull();
        assertThat(publisherRepository.findById(saved.getId())).isPresent();
    }
}
