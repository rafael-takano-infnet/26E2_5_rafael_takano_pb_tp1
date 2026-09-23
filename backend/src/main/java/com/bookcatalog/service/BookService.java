package com.bookcatalog.service;

import com.bookcatalog.domain.Book;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookService {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    List<Book> findByTitle(String title);
    Book create(Book book);
    Book update(Long id, Book book);
    void delete(Long id);
    List<Map<String, Object>> getHistory(Long id);
}
