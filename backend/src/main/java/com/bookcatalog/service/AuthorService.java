package com.bookcatalog.service;

import com.bookcatalog.domain.Author;
import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<Author> findAll();
    Optional<Author> findById(Long id);
    List<Author> findByName(String name);
    Author create(Author author);
    Author update(Long id, Author author);
    void delete(Long id);
}
