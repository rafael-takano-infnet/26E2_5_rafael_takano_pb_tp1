package com.reviewservice.messaging;

import com.reviewservice.config.RabbitMQConfig;
import com.reviewservice.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BookEventListener {

    private static final Logger log = LoggerFactory.getLogger(BookEventListener.class);

    private final ReviewService reviewService;

    public BookEventListener(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @RabbitListener(queues = RabbitMQConfig.BOOK_DELETED_QUEUE)
    public void onBookDeleted(Long bookId) {
        log.info("Evento book.deleted recebido: removendo avaliacoes do livro {}", bookId);
        reviewService.deleteByBookId(bookId);
    }
}
