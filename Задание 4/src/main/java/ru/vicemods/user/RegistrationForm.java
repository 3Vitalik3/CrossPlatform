package ru.vicemods.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrationForm {
    @NotBlank(message = "Введите никнейм")
    @Size(min = 3, max = 40, message = "Никнейм должен содержать от 3 до 40 символов")
    @Pattern(regexp = "^[A-Za-z0-9_.-]+$", message = "Используйте латиницу, цифры и символы _ . -")
    private String username = "";

    @NotBlank(message = "Введите email")
    @Email(message = "Укажите корректный email")
    @Size(max = 190, message = "Email слишком длинный")
    private String email = "";

    @NotBlank(message = "Введите пароль")
    @Size(min = 8, max = 72, message = "Пароль должен содержать от 8 до 72 символов")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s]).+$",
            message = "Пароль должен содержать строчную и заглавную буквы, цифру и специальный символ"
    )
    private String password = "";

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
