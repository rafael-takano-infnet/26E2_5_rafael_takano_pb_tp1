package com.bookcatalog.controller;

import com.bookcatalog.domain.Author;
import com.bookcatalog.dto.AuthorRequestDTO;
import com.bookcatalog.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<Author>> findAll() {
        return ResponseEntity.ok(authorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> findById(@PathVariable Long id) {
        Author author = authorService.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
        return ResponseEntity.ok(author);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Author>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(authorService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Author> create(@Valid @RequestBody AuthorRequestDTO requestDTO) {
        Author author = new Author();
        author.setName(requestDTO.getName());
        author.setBiography(requestDTO.getBiography());
        Author createdAuthor = authorService.create(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@PathVariable Long id, @Valid @RequestBody AuthorRequestDTO requestDTO) {
        Author author = new Author();
        author.setName(requestDTO.getName());
        author.setBiography(requestDTO.getBiography());
        Author updatedAuthor = authorService.update(id, author);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
