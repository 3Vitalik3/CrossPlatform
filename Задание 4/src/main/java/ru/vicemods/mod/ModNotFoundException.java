package ru.vicemods.mod;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ModNotFoundException extends RuntimeException {
    public ModNotFoundException(long id) {
        super("Модификация с id=" + id + " не найдена");
    }
}
