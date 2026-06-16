package com.bookcatalog.controller;

import com.bookcatalog.domain.Author;
import com.bookcatalog.domain.Book;
import com.bookcatalog.domain.Genre;
import com.bookcatalog.domain.Publisher;
import com.bookcatalog.dto.BookRequestDTO;
import com.bookcatalog.dto.BookResponseDTO;
import com.bookcatalog.dto.AuthorDTO;
import com.bookcatalog.dto.GenreDTO;
import com.bookcatalog.dto.PublisherDTO;
import com.bookcatalog.service.BookService;
import com.bookcatalog.service.AuthorService;
import com.bookcatalog.service.GenreService;
import com.bookcatalog.service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final PublisherService publisherService;

    public BookController(BookService bookService, AuthorService authorService,
                         GenreService genreService, PublisherService publisherService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> findAll() {
        List<BookResponseDTO> books = bookService.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> findById(@PathVariable Long id) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        return ResponseEntity.ok(toResponseDTO(book));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDTO>> findByTitle(@RequestParam String title) {
        List<BookResponseDTO> books = bookService.findByTitle(title).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> create(@Valid @RequestBody BookRequestDTO requestDTO) {
        Book book = new Book();
        book.setTitle(requestDTO.getTitle());
        book.setIsbn(requestDTO.getIsbn());
        book.setPublicationYear(requestDTO.getPublicationYear());
        book.setPages(requestDTO.getPages());
        book.setPrice(requestDTO.getPrice());

        Publisher publisher = publisherService.findById(requestDTO.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found with id: " + requestDTO.getPublisherId()));
        book.setPublisher(publisher);

        if (requestDTO.getAuthorIds() != null) {
            Set<Author> authors = requestDTO.getAuthorIds().stream()
                    .map(id -> authorService.findById(id)
                            .orElseThrow(() -> new RuntimeException("Author not found with id: " + id)))
                    .collect(Collectors.toSet());
            book.setAuthors(authors);
        }

        if (requestDTO.getGenreIds() != null) {
            Set<Genre> genres = requestDTO.getGenreIds().stream()
                    .map(id -> genreService.findById(id)
                            .orElseThrow(() -> new RuntimeException("Genre not found with id: " + id)))
                    .collect(Collectors.toSet());
            book.setGenres(genres);
        }

        Book createdBook = bookService.create(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(createdBook));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> update(@PathVariable Long id, @Valid @RequestBody BookRequestDTO requestDTO) {
        Book existingBook = bookService.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        existingBook.setTitle(requestDTO.getTitle());
        existingBook.setIsbn(requestDTO.getIsbn());
        existingBook.setPublicationYear(requestDTO.getPublicationYear());
        existingBook.setPages(requestDTO.getPages());
        existingBook.setPrice(requestDTO.getPrice());

        Publisher publisher = publisherService.findById(requestDTO.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found with id: " + requestDTO.getPublisherId()));
        existingBook.setPublisher(publisher);

        if (requestDTO.getAuthorIds() != null) {
            Set<Author> authors = requestDTO.getAuthorIds().stream()
                    .map(authorId -> authorService.findById(authorId)
                            .orElseThrow(() -> new RuntimeException("Author not found with id: " + authorId)))
                    .collect(Collectors.toSet());
            existingBook.setAuthors(authors);
        }

        if (requestDTO.getGenreIds() != null) {
            Set<Genre> genres = requestDTO.getGenreIds().stream()
                    .map(genreId -> genreService.findById(genreId)
                            .orElseThrow(() -> new RuntimeException("Genre not found with id: " + genreId)))
                    .collect(Collectors.toSet());
            existingBook.setGenres(genres);
        }

        Book updatedBook = bookService.update(id, existingBook);
        return ResponseEntity.ok(toResponseDTO(updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private BookResponseDTO toResponseDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setPages(book.getPages());
        dto.setPrice(book.getPrice());

        if (book.getPublisher() != null) {
            PublisherDTO publisherDTO = new PublisherDTO();
            publisherDTO.setId(book.getPublisher().getId());
            publisherDTO.setName(book.getPublisher().getName());
            publisherDTO.setCountry(book.getPublisher().getCountry());
            dto.setPublisher(publisherDTO);
        }

        if (book.getAuthors() != null) {
            Set<AuthorDTO> authorDTOs = book.getAuthors().stream().map(author -> {
                AuthorDTO authorDTO = new AuthorDTO();
                authorDTO.setId(author.getId());
                authorDTO.setName(author.getName());
                authorDTO.setBiography(author.getBiography());
                return authorDTO;
            }).collect(Collectors.toSet());
            dto.setAuthors(authorDTOs);
        }

        if (book.getGenres() != null) {
            Set<GenreDTO> genreDTOs = book.getGenres().stream().map(genre -> {
                GenreDTO genreDTO = new GenreDTO();
                genreDTO.setId(genre.getId());
                genreDTO.setName(genre.getName());
                genreDTO.setDescription(genre.getDescription());
                return genreDTO;
            }).collect(Collectors.toSet());
            dto.setGenres(genreDTOs);
        }

        return dto;
    }
}
