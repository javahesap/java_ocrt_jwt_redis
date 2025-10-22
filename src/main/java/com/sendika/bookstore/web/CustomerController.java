package com.sendika.bookstore.web;

import com.sendika.bookstore.model.Customer;
import com.sendika.bookstore.repo.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerRepository repo;

    public CustomerController(CustomerRepository repo) {this.repo = repo;}

    @GetMapping
    public List<Customer> all(){return repo.findAll();}

    @GetMapping("{id}")
    public ResponseEntity<Customer> one(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody Customer c){
        return ResponseEntity.ok(repo.save(c));
    }

    @PutMapping("{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @Valid @RequestBody Customer c){
        return repo.findById(id).map(existing -> {
            c.setId(existing.getId());
            return ResponseEntity.ok(repo.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
