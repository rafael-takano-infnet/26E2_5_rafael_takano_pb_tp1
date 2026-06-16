package com.bookcatalog.dto;

import jakarta.validation.constraints.NotBlank;

public class PublisherRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String country;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}
