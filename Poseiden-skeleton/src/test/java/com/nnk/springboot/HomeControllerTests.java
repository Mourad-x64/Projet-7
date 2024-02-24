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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class HomeControllerTests {

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

        User user1 = new User();
        user1 .setFullname("titi");
        user1.setRole("ROLE_ADMIN");
        user1.setUsername("titi");
        user1.setPassword(passwordEncoder.encode("Password_1"));

        userService.save(user1);
    }


    @Test
    public void testShowHome() throws Exception {

        mvc.perform(get("/")
                .with(csrf())
        ).andExpect(status().isOk());


    }

    @Test
    public void testShowAdmin() throws Exception {

        mvc.perform(get("/admin")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin"));


    }

}
