package com.bookcatalog.domain;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "publishers")
public class Publisher extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String country;

    public Publisher() {}

    public Publisher(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}
