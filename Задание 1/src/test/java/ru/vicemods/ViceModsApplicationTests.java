package ru.vicemods;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import ru.vicemods.user.AppUser;
import ru.vicemods.user.AppUserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class ViceModsApplicationTests {

    @Autowired MockMvc mockMvc;
    @Autowired AppUserRepository repository;
    @Autowired PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void rendersBothRequiredPages() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("МОДЫ НА РАДАРЕ")));

        mockMvc.perform(get("/auth"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Регистрация")));
    }

    @Test
    void registersUserAndStoresOnlyPasswordHash() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                        .param("username", "ViceRider")
                        .param("email", "rider@example.com")
                        .param("password", "super-secret"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth?registered"));

        AppUser user = repository.findByEmailIgnoreCase("rider@example.com").orElseThrow();
        assertThat(user.getPasswordHash()).isNotEqualTo("super-secret");
        assertThat(passwordEncoder.matches("super-secret", user.getPasswordHash())).isTrue();
    }

    @Test
    void rejectsInvalidRegistration() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                        .param("username", "!x")
                        .param("email", "wrong")
                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Укажите корректный email")));

        assertThat(repository.count()).isZero();
    }
}
