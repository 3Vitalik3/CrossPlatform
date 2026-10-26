package ru.vicemods.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.vicemods.user.UserService.DuplicateUserException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock AppUserRepository repository;
    @Mock PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(repository, passwordEncoder);
    }

    @Test
    @DisplayName("Регистрация нормализует данные и сохраняет хеш пароля")
    void registersUserWithNormalizedDataAndEncodedPassword() {
        RegistrationForm form = form("  ViceRider  ", "  RIDER@Example.COM  ", "StrongPass1!");
        when(repository.existsByEmailIgnoreCase("rider@example.com")).thenReturn(false);
        when(repository.existsByUsernameIgnoreCase("ViceRider")).thenReturn(false);
        when(passwordEncoder.encode("StrongPass1!")).thenReturn("bcrypt-hash");

        userService.register(form);

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(repository).save(captor.capture());
        AppUser saved = captor.getValue();
        assertThat(saved.getUsername()).isEqualTo("ViceRider");
        assertThat(saved.getEmail()).isEqualTo("rider@example.com");
        assertThat(saved.getPasswordHash()).isEqualTo("bcrypt-hash");
        verify(passwordEncoder).encode("StrongPass1!");
    }

    @Test
    @DisplayName("Регистрация отклоняет занятый email")
    void rejectsDuplicateEmail() {
        RegistrationForm form = form("ViceRider", "rider@example.com", "StrongPass1!");
        when(repository.existsByEmailIgnoreCase("rider@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(form))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessage("Пользователь с таким email уже существует");

        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    @DisplayName("Регистрация отклоняет занятый логин без учёта регистра")
    void rejectsDuplicateUsername() {
        RegistrationForm form = form("ViceRider", "new@example.com", "StrongPass1!");
        when(repository.existsByEmailIgnoreCase("new@example.com")).thenReturn(false);
        when(repository.existsByUsernameIgnoreCase("ViceRider")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(form))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessage("Этот никнейм уже занят");

        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    private RegistrationForm form(String username, String email, String password) {
        RegistrationForm form = new RegistrationForm();
        form.setUsername(username);
        form.setEmail(email);
        form.setPassword(password);
        return form;
    }
}
