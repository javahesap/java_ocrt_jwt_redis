package com.sendika.bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate orderDate;

    @ManyToOne @JoinColumn(name="book_id")
    private Book book;

    @ManyToOne @JoinColumn(name="customer_id")
    private Customer customer;

    // getters/setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public LocalDate getOrderDate(){return orderDate;}
    public void setOrderDate(LocalDate orderDate){this.orderDate=orderDate;}
    public Book getBook(){return book;}
    public void setBook(Book book){this.book=book;}
    public Customer getCustomer(){return customer;}
    public void setCustomer(Customer customer){this.customer=customer;}
}
