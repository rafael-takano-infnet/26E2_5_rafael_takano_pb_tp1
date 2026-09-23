package com.reviewservice.controller;

import com.reviewservice.domain.Review;
import com.reviewservice.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<Review>> findAll(@RequestParam(required = false) Long bookId) {
        if (bookId != null) {
            return ResponseEntity.ok(reviewService.findByBookId(bookId));
        }
        return ResponseEntity.ok(reviewService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> findById(@PathVariable Long id) {
        return reviewService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Review> create(@RequestBody Review review) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(review));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> update(@PathVariable Long id, @RequestBody Review review) {
        return ResponseEntity.ok(reviewService.update(id, review));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
