package com.bookcatalog.controller;
import com.bookcatalog.exception.ResourceNotFoundException;

import com.bookcatalog.domain.Publisher;
import com.bookcatalog.dto.PublisherRequestDTO;
import com.bookcatalog.service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<List<Publisher>> findAll() {
        return ResponseEntity.ok(publisherService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publisher> findById(@PathVariable Long id) {
        Publisher publisher = publisherService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + id));
        return ResponseEntity.ok(publisher);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Publisher>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(publisherService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Publisher> create(@Valid @RequestBody PublisherRequestDTO requestDTO) {
        Publisher publisher = new Publisher();
        publisher.setName(requestDTO.getName());
        publisher.setCountry(requestDTO.getCountry());
        Publisher createdPublisher = publisherService.create(publisher);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPublisher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publisher> update(@PathVariable Long id, @Valid @RequestBody PublisherRequestDTO requestDTO) {
        Publisher publisher = new Publisher();
        publisher.setName(requestDTO.getName());
        publisher.setCountry(requestDTO.getCountry());
        Publisher updatedPublisher = publisherService.update(id, publisher);
        return ResponseEntity.ok(updatedPublisher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        publisherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
