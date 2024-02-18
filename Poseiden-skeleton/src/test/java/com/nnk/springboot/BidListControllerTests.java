package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.UserService;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class BidListControllerTests {

    @Autowired
    BidListRepository bidListRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    BidListService bidListService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mvc;

    @Before
    public void cleanDb(){
        userRepository.deleteAll();
        bidListRepository.deleteAll();
    }

    @Test
    public void bidListControllerTest() throws Exception {

        User user = new User();
        user .setFullname("titi");
        user.setRole("USER");
        user.setUsername("titi");
        user.setPassword(passwordEncoder.encode("Password_1"));

        userService.save(user);

        mvc.perform(post("/bidList/validate")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("account", "account test")
                .param("type", "type test")
                .param("bidQuantity", "20")
                .with(csrf())
        ).andExpect(redirectedUrl("/bidList/list"));

        List<BidList> listResult = bidListService.findAll();
        Assert.assertTrue(listResult.size() > 0);

    }

}
