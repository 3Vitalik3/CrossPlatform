package ru.vicemods.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.vicemods.user.AppUser;
import ru.vicemods.user.AppUserRepository;

@ExtendWith(MockitoExtension.class)
class DatabaseUserDetailsServiceTest {

    @Mock AppUserRepository repository;

    @Test
    @DisplayName("Авторизация загружает пользователя по email")
    void loadsUserForAuthentication() {
        AppUser user = new AppUser("ViceRider", "rider@example.com", "$2a$hash");
        when(repository.findByEmailIgnoreCase("RIDER@example.com")).thenReturn(Optional.of(user));
        DatabaseUserDetailsService service = new DatabaseUserDetailsService(repository);

        UserDetails details = service.loadUserByUsername("  RIDER@example.com  ");

        assertThat(details.getUsername()).isEqualTo("rider@example.com");
        assertThat(details.getPassword()).isEqualTo("$2a$hash");
        assertThat(details.getAuthorities()).extracting("authority").containsExactly("ROLE_USER");
        verify(repository).findByEmailIgnoreCase("RIDER@example.com");
    }

    @Test
    @DisplayName("Авторизация неизвестного пользователя завершается ожидаемой ошибкой")
    void rejectsUnknownUser() {
        when(repository.findByEmailIgnoreCase("missing@example.com")).thenReturn(Optional.empty());
        DatabaseUserDetailsService service = new DatabaseUserDetailsService(repository);

        assertThatThrownBy(() -> service.loadUserByUsername("missing@example.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Пользователь не найден");
    }
}
