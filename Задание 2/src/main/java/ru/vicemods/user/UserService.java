package ru.vicemods.user;

import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(AppUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegistrationForm form) {
        String email = form.getEmail().trim().toLowerCase(Locale.ROOT);
        String username = form.getUsername().trim();
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateUserException("Пользователь с таким email уже существует");
        }
        if (repository.existsByUsernameIgnoreCase(username)) {
            throw new DuplicateUserException("Этот никнейм уже занят");
        }
        repository.save(new AppUser(username, email, passwordEncoder.encode(form.getPassword())));
    }

    public static class DuplicateUserException extends RuntimeException {
        public DuplicateUserException(String message) { super(message); }
    }
}
