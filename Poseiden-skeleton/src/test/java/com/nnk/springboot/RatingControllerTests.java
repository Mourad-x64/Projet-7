package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.CurvePointService;
import com.nnk.springboot.services.RatingService;
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
public class RatingControllerTests {

    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    RatingService ratingService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mvc;

    private Rating rating;

    @Before
    public void cleanDb(){
        userRepository.deleteAll();
        ratingRepository.deleteAll();

        User user = new User();
        user .setFullname("titi");
        user.setRole("ROLE_USER");
        user.setUsername("titi");
        user.setPassword(passwordEncoder.encode("Password_1"));

        userService.save(user);

        Rating rating = new Rating();
        rating.setMoodysRating("A");
        rating.setSandPRating("B");
        rating.setFitchRating("C");

        this.rating = ratingService.save(rating);
    }

    @Test
    public void showRatingListTest() throws Exception {
        mvc.perform(get("/rating/list")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void showRatingAddTest() throws Exception {
        mvc.perform(get("/rating/add")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void showRatingUpdateTest() throws Exception {
        mvc.perform(get("/rating/update/"+rating.getRatingId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void deleteRatingTest() throws Exception {

        mvc.perform(get("/rating/delete/"+rating.getRatingId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(redirectedUrl("/rating/list"));

        Optional<Rating> listResult = ratingService.findById(rating.getRatingId());
        Assert.assertFalse(listResult.isPresent());

    }

    @Test
    public void addRatingTest() throws Exception {

        mvc.perform(post("/rating/validate")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("moodysRating", "50")
                .param("sandPRating", "40")
                .param("fitchRating", "70")

                .with(csrf())
        ).andExpect(redirectedUrl("/rating/list"));

        List<Rating> listResult = ratingService.findAll();
        Assert.assertTrue(listResult.size() > 0);

    }

    @Test
    public void updateRatingTest() throws Exception {

        Rating rating = new Rating();
        rating.setMoodysRating("A");
        rating.setSandPRating("B");
        rating.setFitchRating("C");

        Rating rating1 = ratingService.save(rating);


        mvc.perform(post("/rating/update/"+rating1.getRatingId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("moodysRating", "B")
                .param("sandPRating", "AA")
                .param("fitchRating", "C")
                .with(csrf())
        ).andExpect(redirectedUrl("/rating/list"));

        Optional<Rating> rating2 = ratingService.findById(rating1.getRatingId());
        if(rating2.isPresent()){
            Assert.assertEquals("AA", rating2.get().getSandPRating());
        }

    }


}
