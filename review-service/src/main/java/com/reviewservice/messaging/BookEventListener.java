package com.reviewservice.messaging;

import com.reviewservice.config.RabbitMQConfig;
import com.reviewservice.service.ReviewService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BookEventListener {

    private final ReviewService reviewService;

    public BookEventListener(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @RabbitListener(queues = RabbitMQConfig.BOOK_DELETED_QUEUE)
    public void onBookDeleted(Long bookId) {
        reviewService.deleteByBookId(bookId);
    }
}
