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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
public class TradeControllerTests {

    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    TradeService tradeService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mvc;

    private Trade trade;

    @Before
    public void cleanDb(){
        userRepository.deleteAll();
        tradeRepository.deleteAll();

        User user = new User();
        user .setFullname("titi");
        user.setRole("ROLE_USER");
        user.setUsername("titi");
        user.setPassword(passwordEncoder.encode("Password_1"));

        userService.save(user);

        Trade trade = new Trade();
        trade.setAccount("account test");
        trade.setType("type test");
        trade.setBuyQuantity(70d);

        this.trade = tradeService.save(trade);
    }

    @Test
    public void showTradeListTest() throws Exception {
        mvc.perform(get("/trade/list")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void showTradeAddTest() throws Exception {
        mvc.perform(get("/trade/add")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void showTradeUpdateTest() throws Exception {
        mvc.perform(get("/trade/update/"+trade.getTradeId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void deleteTradeTest() throws Exception {

        mvc.perform(get("/trade/delete/"+trade.getTradeId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(redirectedUrl("/trade/list"));

        Optional<Trade> listResult = tradeService.findById(trade.getTradeId());
        Assert.assertFalse(listResult.isPresent());

    }


    @Test
    public void addTradeTest() throws Exception {

        //error
        mvc.perform(post("/trade/validate")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("account", "account test")
                .param("type", "type test")
                .with(csrf())
        ).andExpect(MockMvcResultMatchers.view().name("trade/add"));

        //ok
        mvc.perform(post("/trade/validate")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("account", "account test")
                .param("type", "type test")
                .param("BuyQuantity", "70")
                .with(csrf())
        ).andExpect(redirectedUrl("/trade/list"));

        List<Trade> listResult = tradeService.findAll();
        Assert.assertTrue(listResult.size() == 2);

    }

    @Test
    public void updateTradeTest() throws Exception {

        Trade trade = new Trade();
        trade.setAccount("account test");
        trade.setType("type test");
        trade.setBuyQuantity(70d);

        Trade trade1 = tradeService.save(trade);

        //error
        mvc.perform(post("/trade/update/"+trade1.getTradeId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("account", "account test")
                .param("type", "type test")
                .with(csrf())
        ).andExpect(MockMvcResultMatchers.view().name("trade/update"));

        //ok
        mvc.perform(post("/trade/update/"+trade1.getTradeId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("account", "account test")
                .param("type", "type test")
                .param("BuyQuantity", "90")
                .with(csrf())
        ).andExpect(redirectedUrl("/trade/list"));

        Optional<Trade> trade2 = tradeService.findById(trade1.getTradeId());
        if(trade2.isPresent()){
            Assert.assertEquals(90d, trade2.get().getBuyQuantity(), 90d);
        }

    }


}
