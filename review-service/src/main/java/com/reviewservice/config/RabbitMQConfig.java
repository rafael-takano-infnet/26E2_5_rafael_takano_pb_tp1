package com.reviewservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String BOOK_EVENTS_EXCHANGE = "book-events";
    public static final String BOOK_DELETED_ROUTING_KEY = "book.deleted";
    public static final String BOOK_DELETED_QUEUE = "review-service.book.deleted";

    @Bean
    public TopicExchange bookEventsExchange() {
        return new TopicExchange(BOOK_EVENTS_EXCHANGE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue bookDeletedQueue() {
        return new Queue(BOOK_DELETED_QUEUE);
    }

    @Bean
    public Binding bookDeletedBinding(Queue bookDeletedQueue, TopicExchange bookEventsExchange) {
        return BindingBuilder.bind(bookDeletedQueue).to(bookEventsExchange).with(BOOK_DELETED_ROUTING_KEY);
    }
}
