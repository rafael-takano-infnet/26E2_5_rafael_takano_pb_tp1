package com.reviewservice.service;

import com.reviewservice.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<Review> findAll();
    Optional<Review> findById(Long id);
    List<Review> findByBookId(Long bookId);
    Review create(Review review);
    Review update(Long id, Review review);
    void delete(Long id);
    void deleteByBookId(Long bookId);
}
