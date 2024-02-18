package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class UserServiceTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Before
    public void cleanDb(){
        userRepository.deleteAll();
    }


    @Test
    public void userTest() {
        User user = new User();

        user.setUsername("titi");
        user.setRole("USER");
        user.setFullname("titi");
        user.setPassword(passwordEncoder.encode("Password_1"));

        // Save
        user = userService.save(user);
        Assert.assertNotNull(user.getUserId());
        Assert.assertEquals(user.getFullname(), "titi");

        // Update
        user.setFullname("toto");
        user = userService.save(user);
        Assert.assertEquals(user.getFullname(), "toto");

        // Find
        Integer id = user.getUserId();
        List<User> listResult = userService.findAll();
        Assert.assertTrue(listResult.size() > 0);
        Optional<User> user1 = userService.findById(id);
        User newUser;
        if (user1.isPresent()){
            newUser = user1.get();
            Assert.assertEquals(newUser.getUserId(), id, id);
        }

        // Delete
        userService.delete(user);
        Optional<User> user2 = userService.findById(id);
        Assert.assertFalse(user2.isPresent());

    }

}
