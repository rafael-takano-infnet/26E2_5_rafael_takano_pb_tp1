package com.reviewservice.service.impl;

import com.reviewservice.domain.Review;
import com.reviewservice.repository.ReviewRepository;
import com.reviewservice.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public List<Review> findByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    @Override
    public Review create(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Review update(Long id, Review reviewDetails) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));

        review.setBookId(reviewDetails.getBookId());
        review.setRating(reviewDetails.getRating());
        review.setComment(reviewDetails.getComment());

        return reviewRepository.save(review);
    }

    @Override
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public void deleteByBookId(Long bookId) {
        reviewRepository.deleteByBookId(bookId);
    }
}
