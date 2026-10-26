package ru.vicemods.mod;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "mod_concepts")
public class ModConcept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 40)
    private String category;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, length = 40)
    private String author;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected ModConcept() {
    }

    public ModConcept(String title, String category, String description, String author) {
        update(title, category, description, author);
    }

    @PrePersist
    void assignCreatedAt() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public void update(String title, String category, String description, String author) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.author = author;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getAuthor() { return author; }
    public Instant getCreatedAt() { return createdAt; }
}
