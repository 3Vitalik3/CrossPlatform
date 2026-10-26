package ru.vicemods.user;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationFormValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Корректная форма проходит все проверки")
    void acceptsValidForm() {
        assertThat(validator.validate(validForm())).isEmpty();
    }

    @Test
    @DisplayName("Email должен иметь корректный формат")
    void rejectsInvalidEmail() {
        RegistrationForm form = validForm();
        form.setEmail("not-an-email");

        assertThat(messagesFor(form, "email")).contains("Укажите корректный email");
    }

    @Test
    @DisplayName("Логин принимает только разрешённые символы")
    void rejectsInvalidUsernameCharacters() {
        RegistrationForm form = validForm();
        form.setUsername("vice rider!");

        assertThat(messagesFor(form, "username"))
                .contains("Используйте латиницу, цифры и символы _ . -");
    }

    @Test
    @DisplayName("Логин должен содержать от 3 до 40 символов")
    void rejectsTooShortUsername() {
        RegistrationForm form = validForm();
        form.setUsername("vr");

        assertThat(messagesFor(form, "username"))
                .contains("Никнейм должен содержать от 3 до 40 символов");
    }

    @Test
    @DisplayName("Пароль должен быть не короче восьми символов")
    void rejectsShortPassword() {
        RegistrationForm form = validForm();
        form.setPassword("Aa1!");

        assertThat(messagesFor(form, "password"))
                .contains("Пароль должен содержать от 8 до 72 символов");
    }

    @Test
    @DisplayName("Пароль должен содержать буквы разных регистров, цифру и спецсимвол")
    void rejectsWeakPasswords() {
        assertThat(messagesFor(formWithPassword("password1!"), "password"))
                .contains(complexityMessage());
        assertThat(messagesFor(formWithPassword("PASSWORD1!"), "password"))
                .contains(complexityMessage());
        assertThat(messagesFor(formWithPassword("Password!!"), "password"))
                .contains(complexityMessage());
        assertThat(messagesFor(formWithPassword("Password12"), "password"))
                .contains(complexityMessage());
    }

    private RegistrationForm validForm() {
        return formWithPassword("StrongPass1!");
    }

    private RegistrationForm formWithPassword(String password) {
        RegistrationForm form = new RegistrationForm();
        form.setUsername("ViceRider");
        form.setEmail("rider@example.com");
        form.setPassword(password);
        return form;
    }

    private Set<String> messagesFor(RegistrationForm form, String field) {
        return validator.validate(form).stream()
                .filter(violation -> violation.getPropertyPath().toString().equals(field))
                .map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());
    }

    private String complexityMessage() {
        return "Пароль должен содержать строчную и заглавную буквы, цифру и специальный символ";
    }
}
