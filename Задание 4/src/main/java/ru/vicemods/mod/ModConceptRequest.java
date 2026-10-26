package ru.vicemods.mod;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ModConceptRequest(
        @NotBlank(message = "Название обязательно")
        @Size(max = 100, message = "Название не должно превышать 100 символов")
        String title,

        @NotBlank(message = "Категория обязательна")
        @Size(max = 40, message = "Категория не должна превышать 40 символов")
        String category,

        @NotBlank(message = "Описание обязательно")
        @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
        String description,

        @NotBlank(message = "Автор обязателен")
        @Size(max = 40, message = "Имя автора не должно превышать 40 символов")
        String author
) {
}
