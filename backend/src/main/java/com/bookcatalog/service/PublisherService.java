package com.bookcatalog.service;

import com.bookcatalog.domain.Publisher;
import java.util.List;
import java.util.Optional;

public interface PublisherService {
    List<Publisher> findAll();
    Optional<Publisher> findById(Long id);
    List<Publisher> findByName(String name);
    Publisher create(Publisher publisher);
    Publisher update(Long id, Publisher publisher);
    void delete(Long id);
}
