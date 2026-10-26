package ru.vicemods.mod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class ModConceptServiceTest {

    @Mock ModConceptRepository repository;

    private ModConceptService service;

    @BeforeEach
    void setUp() {
        service = new ModConceptService(repository);
    }

    @Test
    @DisplayName("CREATE сохраняет новую модификацию")
    void createsMod() {
        ModConceptRequest request = request("  Neon Cars  ", "  transport  ");
        when(repository.save(any(ModConcept.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ModConcept created = service.create(request);

        assertThat(created.getTitle()).isEqualTo("Neon Cars");
        assertThat(created.getCategory()).isEqualTo("transport");
        assertThat(created.getDescription()).isEqualTo("Описание мода");
        verify(repository).save(created);
    }

    @Test
    @DisplayName("READ возвращает список из репозитория")
    void readsAllMods() {
        ModConcept mod = new ModConcept("Neon Cars", "transport", "Описание", "ViceRider");
        when(repository.findAll(any(Sort.class))).thenReturn(List.of(mod));

        assertThat(service.findAll()).containsExactly(mod);
    }

    @Test
    @DisplayName("UPDATE изменяет существующую модификацию")
    void updatesMod() {
        ModConcept existing = new ModConcept("Old", "other", "Старое описание", "OldAuthor");
        when(repository.findById(7L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        ModConcept updated = service.update(7L, request("New title", "visual"));

        assertThat(updated.getTitle()).isEqualTo("New title");
        assertThat(updated.getCategory()).isEqualTo("visual");
        assertThat(updated.getAuthor()).isEqualTo("ViceRider");
        verify(repository).save(existing);
    }

    @Test
    @DisplayName("DELETE удаляет найденную модификацию")
    void deletesMod() {
        ModConcept existing = new ModConcept("Old", "other", "Описание", "Author");
        when(repository.findById(3L)).thenReturn(Optional.of(existing));

        service.delete(3L);

        verify(repository).delete(existing);
    }

    @Test
    @DisplayName("Операция с неизвестным id возвращает ошибку 404")
    void rejectsUnknownId() {
        when(repository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(404L))
                .isInstanceOf(ModNotFoundException.class)
                .hasMessage("Модификация с id=404 не найдена");
    }

    private ModConceptRequest request(String title, String category) {
        return new ModConceptRequest(title, category, "  Описание мода  ", "  ViceRider  ");
    }
}
