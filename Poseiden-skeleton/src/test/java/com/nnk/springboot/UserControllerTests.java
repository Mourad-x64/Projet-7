package com.nnk.springboot;

import com.nnk.springboot.domain.*;
import com.nnk.springboot.repositories.*;
import com.nnk.springboot.services.*;
import org.junit.Assert;
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

import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class UserControllerTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mvc;

    private User user;


    @Before
    public void cleanDb(){

        userRepository.deleteAll();

        User user1 = new User();
        user1 .setFullname("titi");
        user1.setRole("ROLE_USER");
        user1.setUsername("titi");
        user1.setPassword(passwordEncoder.encode("Password_1"));

        userService.save(user1);

        User user2 = new User();
        user2 .setFullname("toto");
        user2.setRole("ROLE_USER");
        user2.setUsername("toto");
        user2.setPassword(passwordEncoder.encode("Password_1"));

        this.user = userService.save(user2);
    }

    @Test
    public void showUserListTest() throws Exception {
        mvc.perform(get("/user/list")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void showUserAddTest() throws Exception {
        mvc.perform(get("/user/add")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void showUserUpdateTest() throws Exception {
        mvc.perform(get("/user/update/"+user.getUserId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void deleteUserTest() throws Exception {

        mvc.perform(get("/user/delete/"+user.getUserId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(redirectedUrl("/user/list"));

        Optional<User> listResult = userService.findById(user.getUserId());
        Assert.assertFalse(listResult.isPresent());

    }



    @Test
    public void addUserTest() throws Exception {

        mvc.perform(post("/user/validate")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("fullname", "user test")
                .param("username", "user test")
                .param("password", "Password_1")
                .param("role", "ROLE_USER")

                .with(csrf())
        ).andExpect(redirectedUrl("/user/list"));

        List<User> listResult = userService.findAll();
        Assert.assertTrue(listResult.size() > 0);

    }

    @Test
    public void updateUserTest() throws Exception {

        User user = new User();
        user.setFullname("user test");
        user.setUsername("user test");
        user.setRole("ROLE_USER");
        user.setPassword("Password_1");

        User user1 = userService.save(user);


        mvc.perform(post("/user/update/"+user.getUserId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("fullname", "user test")
                .param("username", "username test")
                .param("password", "Password_1")
                .param("role", "ROLE_USER")
                .with(csrf())
        ).andExpect(redirectedUrl("/user/list"));

        Optional<User> user2 = userService.findById(user1.getUserId());
        if(user2.isPresent()){
            Assert.assertEquals("username test", user2.get().getUsername());
        }

    }


}
