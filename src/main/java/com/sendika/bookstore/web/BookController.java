package com.sendika.bookstore.web;

import com.sendika.bookstore.model.Book;
import com.sendika.bookstore.repo.BookRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookRepository repo;

    public BookController(BookRepository repo) {this.repo = repo;}

    @GetMapping
    public List<Book> all(){return repo.findAll();}

    @GetMapping("{id}")
    public ResponseEntity<Book> one(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody Book b){
        return ResponseEntity.ok(repo.save(b));
    }

    @PutMapping("{id}")
    public ResponseEntity<Book> update(@PathVariable Long id, @Valid @RequestBody Book b){
        return repo.findById(id).map(existing -> {
            b.setId(existing.getId());
            return ResponseEntity.ok(repo.save(b));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
