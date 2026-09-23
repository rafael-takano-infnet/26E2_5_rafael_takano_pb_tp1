package com.bookcatalog.service.impl;
import com.bookcatalog.exception.ResourceNotFoundException;

import com.bookcatalog.config.RabbitMQConfig;
import com.bookcatalog.domain.Book;
import com.bookcatalog.repository.BookRepository;
import com.bookcatalog.service.BookService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final RabbitTemplate rabbitTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    public BookServiceImpl(BookRepository bookRepository, RabbitTemplate rabbitTemplate) {
        this.bookRepository = bookRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book update(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        book.setTitle(bookDetails.getTitle());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setPages(bookDetails.getPages());
        book.setPrice(bookDetails.getPrice());
        book.setPublisher(bookDetails.getPublisher());
        book.setAuthors(bookDetails.getAuthors());
        book.setGenres(bookDetails.getGenres());

        return bookRepository.save(book);
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
        rabbitTemplate.convertAndSend(RabbitMQConfig.BOOK_EVENTS_EXCHANGE, RabbitMQConfig.BOOK_DELETED_ROUTING_KEY, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getHistory(Long id) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Object[]> results = auditReader.createQuery()
                .forRevisionsOfEntity(Book.class, false, true)
                .add(AuditEntity.id().eq(id))
                .getResultList();

        List<Map<String, Object>> history = new ArrayList<>();
        for (Object[] row : results) {
            Book book = (Book) row[0];
            DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) row[1];
            RevisionType revisionType = (RevisionType) row[2];

            Map<String, Object> entry = new HashMap<>();
            entry.put("id", book.getId());
            entry.put("title", book.getTitle());
            entry.put("isbn", book.getIsbn());
            entry.put("revision", revisionEntity.getId());
            entry.put("timestamp", revisionEntity.getTimestamp());
            entry.put("revisionType", revisionType.name());
            history.add(entry);
        }
        return history;
    }
}
