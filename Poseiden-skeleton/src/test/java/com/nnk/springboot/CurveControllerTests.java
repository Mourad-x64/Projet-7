package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.CurvePointService;
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
public class CurveControllerTests {

    @Autowired
    CurvePointRepository curvePointRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    CurvePointService curvePointService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mvc;

    private CurvePoint curvePoint;

    @Before
    public void cleanDb(){
        userRepository.deleteAll();
        curvePointRepository.deleteAll();

        User user = new User();
        user .setFullname("titi");
        user.setRole("ROLE_USER");
        user.setUsername("titi");
        user.setPassword(passwordEncoder.encode("Password_1"));

        userService.save(user);

        CurvePoint curve = new CurvePoint();
        curve.setTerm(60d);
        curve.setValue(20d);

        this.curvePoint = curvePointService.save(curve);
    }

    @Test
    public void showCuvePointListTest() throws Exception {
        mvc.perform(get("/curvePoint/list")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void showCurvePointAddTest() throws Exception {
        mvc.perform(get("/curvePoint/add")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void showCurvePointUpdateTest() throws Exception {
        mvc.perform(get("/curvePoint/update/"+curvePoint.getCurvePointId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void deleteBidListTest() throws Exception {

        mvc.perform(get("/curvePoint/delete/"+curvePoint.getCurvePointId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(redirectedUrl("/curvePoint/list"));

        Optional<CurvePoint> listResult = curvePointService.findById(curvePoint.getCurvePointId());
        Assert.assertFalse(listResult.isPresent());

    }

    @Test
    public void addCurvePointTest() throws Exception {

        mvc.perform(post("/curvePoint/validate")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("term", "50")
                .param("value", "20")

                .with(csrf())
        ).andExpect(redirectedUrl("/curvePoint/list"));

        List<CurvePoint> listResult = curvePointService.findAll();
        Assert.assertTrue(listResult.size() > 0);

    }

    @Test
    public void updateCurvePointTest() throws Exception {

        CurvePoint curve = new CurvePoint();
        curve.setTerm(60d);
        curve.setValue(20d);

        CurvePoint curve1 = curvePointService.save(curve);


        mvc.perform(post("/curvePoint/update/"+curve1.getCurvePointId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("term", "60")
                .param("value", "40")
                .with(csrf())
        ).andExpect(redirectedUrl("/curvePoint/list"));

        Optional<CurvePoint> curve2 = curvePointService.findById(curve1.getCurvePointId());
        if(curve2.isPresent()){
            Assert.assertEquals(40d, curve2.get().getValue(), 40d);
        }

    }


}
