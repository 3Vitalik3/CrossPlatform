package ru.vicemods.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vicemods.user.RegistrationForm;
import ru.vicemods.user.UserService;
import ru.vicemods.user.UserService.DuplicateUserException;

@Controller
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public String register(@Valid RegistrationForm registrationForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activeMode", "register");
            return "auth";
        }
        try {
            userService.register(registrationForm);
        } catch (DuplicateUserException exception) {
            model.addAttribute("registrationError", exception.getMessage());
            model.addAttribute("activeMode", "register");
            return "auth";
        }
        return "redirect:/auth?registered";
    }
}
