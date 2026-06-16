package com.bookcatalog.dto;

import java.math.BigDecimal;
import java.util.Set;

public class BookResponseDTO {

    private Long id;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private Integer pages;
    private BigDecimal price;
    private PublisherDTO publisher;
    private Set<AuthorDTO> authors;
    private Set<GenreDTO> genres;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }
    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public PublisherDTO getPublisher() { return publisher; }
    public void setPublisher(PublisherDTO publisher) { this.publisher = publisher; }
    public Set<AuthorDTO> getAuthors() { return authors; }
    public void setAuthors(Set<AuthorDTO> authors) { this.authors = authors; }
    public Set<GenreDTO> getGenres() { return genres; }
    public void setGenres(Set<GenreDTO> genres) { this.genres = genres; }
}
