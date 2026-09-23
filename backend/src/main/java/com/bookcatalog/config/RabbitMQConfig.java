package com.bookcatalog.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String BOOK_EVENTS_EXCHANGE = "book-events";
    public static final String BOOK_DELETED_ROUTING_KEY = "book.deleted";

    @Bean
    public TopicExchange bookEventsExchange() {
        return new TopicExchange(BOOK_EVENTS_EXCHANGE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
