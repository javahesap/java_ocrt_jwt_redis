package com.sendika.bookstore.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "snaps")
public class Snap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;       // ana başlık
    private String subtitle;    // alt başlık
    private String filename;    // diskteki dosya adı (PNG)
    private Instant createdAt = Instant.now();

    public Snap() {}
    public Snap(String title, String subtitle, String filename) {
        this.title = title;
        this.subtitle = subtitle;
        this.filename = filename;
    }

    // getters/setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public String getFilename() { return filename; }
    public Instant getCreatedAt() { return createdAt; }
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public void setFilename(String filename) { this.filename = filename; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
