package com.bookcatalog.controller;

import com.bookcatalog.domain.Genre;
import com.bookcatalog.dto.GenreRequestDTO;
import com.bookcatalog.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public ResponseEntity<List<Genre>> findAll() {
        return ResponseEntity.ok(genreService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> findById(@PathVariable Long id) {
        Genre genre = genreService.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + id));
        return ResponseEntity.ok(genre);
    }

    @PostMapping
    public ResponseEntity<Genre> create(@Valid @RequestBody GenreRequestDTO requestDTO) {
        Genre genre = new Genre();
        genre.setName(requestDTO.getName());
        genre.setDescription(requestDTO.getDescription());
        Genre createdGenre = genreService.create(genre);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGenre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genre> update(@PathVariable Long id, @Valid @RequestBody GenreRequestDTO requestDTO) {
        Genre genre = new Genre();
        genre.setName(requestDTO.getName());
        genre.setDescription(requestDTO.getDescription());
        Genre updatedGenre = genreService.update(id, genre);
        return ResponseEntity.ok(updatedGenre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        genreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
