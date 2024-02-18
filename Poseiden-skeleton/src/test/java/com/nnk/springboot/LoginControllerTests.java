package com.nnk.springboot;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class LoginControllerTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mvc;

    @Before
    public void cleanDb(){
        userRepository.deleteAll();
    }

    @Test
    public void testShowLoginForm() throws Exception {

        mvc.perform(get("/login")
                        .with(csrf())
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"));

    }

    @Test
    public void testLogin() throws Exception {

        User user = new User();

        user .setFullname("titi");
        user.setRole("USER");
        user.setUsername("titi");
        user.setPassword(passwordEncoder.encode("Password_1"));

        userService.save(user);

        mvc.perform(post("/login")
                .param("username", "titi")
                .param("password", "Password_1")
                .with(csrf())
        ).andExpect(redirectedUrl("/bidList/list"));

        mvc.perform(post("/login")
                .param("username", "titi")
                .param("password", "tutu2")
                .with(csrf())
        ).andExpect(redirectedUrl("/login?error"));

    }


}
