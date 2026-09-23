package com.bookcatalog.service.impl;
import com.bookcatalog.exception.ResourceNotFoundException;

import com.bookcatalog.domain.Genre;
import com.bookcatalog.repository.GenreRepository;
import com.bookcatalog.service.GenreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }

    @Override
    public Optional<Genre> findByName(String name) {
        return genreRepository.findByName(name);
    }

    @Override
    public Genre create(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public Genre update(Long id, Genre genreDetails) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));

        genre.setName(genreDetails.getName());
        genre.setDescription(genreDetails.getDescription());

        return genreRepository.save(genre);
    }

    @Override
    public void delete(Long id) {
        genreRepository.deleteById(id);
    }
}
