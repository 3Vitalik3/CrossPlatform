package ru.vicemods.mod;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mods")
public class ModConceptController {

    private final ModConceptService service;

    public ModConceptController(ModConceptService service) {
        this.service = service;
    }

    @GetMapping
    public List<ModConcept> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ModConcept findById(@PathVariable long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<ModConcept> create(@Valid @RequestBody ModConceptRequest request) {
        ModConcept created = service.create(request);
        return ResponseEntity.created(URI.create("/api/mods/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ModConcept update(@PathVariable long id, @Valid @RequestBody ModConceptRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
