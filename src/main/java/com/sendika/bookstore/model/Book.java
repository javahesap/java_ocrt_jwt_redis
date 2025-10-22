package com.sendika.bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "books")
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String authors;

    @NotBlank
    private String isbn;

    @NotBlank
    private String name;

    private BigDecimal price;

    @NotNull
    private LocalDate publishedOn;

    @NotBlank
    private String publisher;

    // getters/setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getAuthors() {return authors;}
    public void setAuthors(String authors) {this.authors = authors;}
    public String getIsbn() {return isbn;}
    public void setIsbn(String isbn) {this.isbn = isbn;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}
    public LocalDate getPublishedOn() {return publishedOn;}
    public void setPublishedOn(LocalDate publishedOn) {this.publishedOn = publishedOn;}
    public String getPublisher() {return publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}
}
