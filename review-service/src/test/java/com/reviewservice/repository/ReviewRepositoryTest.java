package com.reviewservice.repository;

import com.reviewservice.domain.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void savesAndFindsReview() {
        Review review = new Review();
        review.setBookId(1L);
        review.setRating(5);
        review.setComment("Ótimo livro");

        Review saved = reviewRepository.save(review);

        assertThat(saved.getId()).isNotNull();
        assertThat(reviewRepository.findByBookId(1L)).hasSize(1);
    }
}
