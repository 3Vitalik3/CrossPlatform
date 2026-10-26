package ru.vicemods.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired AppUserRepository repository;

    @Test
    @DisplayName("Пользователь сохраняется и находится по email без учёта регистра")
    void savesAndFindsUserCaseInsensitively() {
        repository.saveAndFlush(new AppUser("ViceRider", "rider@example.com", "bcrypt-hash"));

        AppUser found = repository.findByEmailIgnoreCase("RIDER@EXAMPLE.COM").orElseThrow();

        assertThat(found.getId()).isNotNull();
        assertThat(found.getUsername()).isEqualTo("ViceRider");
        assertThat(repository.existsByUsernameIgnoreCase("vicerider")).isTrue();
        assertThat(repository.existsByEmailIgnoreCase("RIDER@example.com")).isTrue();
    }

    @Test
    @DisplayName("Ограничение базы данных не допускает одинаковый email")
    void databaseRejectsDuplicateEmail() {
        repository.saveAndFlush(new AppUser("FirstRider", "same@example.com", "hash-1"));

        assertThatThrownBy(() -> repository.saveAndFlush(
                new AppUser("SecondRider", "same@example.com", "hash-2")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Ограничение базы данных не допускает одинаковый логин")
    void databaseRejectsDuplicateUsername() {
        repository.saveAndFlush(new AppUser("SameRider", "first@example.com", "hash-1"));

        assertThatThrownBy(() -> repository.saveAndFlush(
                new AppUser("SameRider", "second@example.com", "hash-2")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
