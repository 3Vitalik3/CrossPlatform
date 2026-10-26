package ru.vicemods.mod;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModConceptService {

    private final ModConceptRepository repository;

    public ModConceptService(ModConceptRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ModConcept> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Transactional(readOnly = true)
    public ModConcept findById(long id) {
        return repository.findById(id).orElseThrow(() -> new ModNotFoundException(id));
    }

    @Transactional
    public ModConcept create(ModConceptRequest request) {
        ModConcept mod = new ModConcept(
                request.title().trim(), request.category().trim(),
                request.description().trim(), request.author().trim());
        return repository.save(mod);
    }

    @Transactional
    public ModConcept update(long id, ModConceptRequest request) {
        ModConcept mod = findById(id);
        mod.update(
                request.title().trim(), request.category().trim(),
                request.description().trim(), request.author().trim());
        return repository.save(mod);
    }

    @Transactional
    public void delete(long id) {
        ModConcept mod = findById(id);
        repository.delete(mod);
    }
}
