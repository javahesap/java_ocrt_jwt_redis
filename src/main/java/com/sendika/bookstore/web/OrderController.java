package com.sendika.bookstore.web;

import com.sendika.bookstore.model.Book;
import com.sendika.bookstore.model.Customer;
import com.sendika.bookstore.model.Order;
import com.sendika.bookstore.repo.BookRepository;
import com.sendika.bookstore.repo.CustomerRepository;
import com.sendika.bookstore.repo.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository repo;
    private final BookRepository bookRepo;
    private final CustomerRepository custRepo;

    public OrderController(OrderRepository repo, BookRepository bookRepo, CustomerRepository custRepo) {
        this.repo = repo;
        this.bookRepo = bookRepo;
        this.custRepo = custRepo;
    }

    @GetMapping
    public List<Order> all(){return repo.findAll();}

    @GetMapping("{id}")
    public ResponseEntity<Order> one(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Order> create(@Valid @RequestBody Order o){
        // ensure book/customer exist
        if (o.getBook()!=null && o.getBook().getId()!=null) {
            Book b = bookRepo.findById(o.getBook().getId()).orElse(null);
            o.setBook(b);
        }
        if (o.getCustomer()!=null && o.getCustomer().getId()!=null) {
            Customer c = custRepo.findById(o.getCustomer().getId()).orElse(null);
            o.setCustomer(c);
        }
        return ResponseEntity.ok(repo.save(o));
    }

    @PutMapping("{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @Valid @RequestBody Order o){
        return repo.findById(id).map(existing -> {
            o.setId(existing.getId());
            if (o.getBook()!=null && o.getBook().getId()!=null) {
                o.setBook(bookRepo.findById(o.getBook().getId()).orElse(null));
            }
            if (o.getCustomer()!=null && o.getCustomer().getId()!=null) {
                o.setCustomer(custRepo.findById(o.getCustomer().getId()).orElse(null));
            }
            return ResponseEntity.ok(repo.save(o));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
