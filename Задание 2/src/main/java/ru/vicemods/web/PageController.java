package ru.vicemods.web;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vicemods.user.AppUserRepository;
import ru.vicemods.user.RegistrationForm;

@Controller
public class PageController {
    private final AppUserRepository repository;

    public PageController(AppUserRepository repository) { this.repository = repository; }

    @GetMapping("/")
    public String home(Principal principal, Model model) {
        if (principal != null) {
            repository.findByEmailIgnoreCase(principal.getName())
                    .ifPresent(user -> model.addAttribute("currentUsername", user.getUsername()));
        }
        return "index";
    }

    @GetMapping("/auth")
    public String auth(@RequestParam(defaultValue = "login") String mode, Model model) {
        if (!model.containsAttribute("registrationForm")) {
            model.addAttribute("registrationForm", new RegistrationForm());
        }
        model.addAttribute("activeMode", "register".equals(mode) ? "register" : "login");
        return "auth";
    }
}
