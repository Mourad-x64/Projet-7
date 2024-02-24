package com.nnk.springboot;

import com.nnk.springboot.controllers.BidListController;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.UserService;
import jakarta.validation.constraints.Null;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    private BidList bidList;

    @Before
    public void cleanDb(){
        userRepository.deleteAll();
        bidListRepository.deleteAll();

        User user = new User();
        user .setFullname("titi");
        user.setRole("ROLE_USER");
        user.setUsername("titi");
        user.setPassword(passwordEncoder.encode("Password_1"));

        userService.save(user);

        BidList bid = new BidList();
        bid.setAccount("account test");
        bid.setType("type test");
        bid.setBidQuantity(20d);

        this.bidList = bidListService.save(bid);
    }

    @Test
    public void showBidListTest() throws Exception {
        mvc.perform(get("/bidList/list")
                        .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                        .with(csrf())
                ).andExpect(status().isOk());

    }

    @Test
    public void showBidListAddTest() throws Exception {
        mvc.perform(get("/bidList/add")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void showBidListUpdateTest() throws Exception {
        mvc.perform(get("/bidList/update/"+bidList.getBidListId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void deleteBidListTest() throws Exception {

        mvc.perform(get("/bidList/delete/"+bidList.getBidListId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(redirectedUrl("/bidList/list"));

        Optional<BidList> listResult = bidListService.findById(bidList.getBidListId());
        Assert.assertFalse(listResult.isPresent());

    }

    @Test
    public void addBidListTest() throws Exception {

        //error
        mvc.perform(post("/bidList/validate")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("account", "account test")
                .param("type", "type test")
                .with(csrf())
        ).andExpect(MockMvcResultMatchers.view().name("bidList/add"));

        //ok
        mvc.perform(post("/bidList/validate")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("account", "account test")
                .param("type", "type test")
                .param("bidQuantity", "20")
                .with(csrf())
        ).andExpect(redirectedUrl("/bidList/list"));

        List<BidList> listResult = bidListService.findAll();
        Assert.assertTrue(listResult.size() == 2);

    }

    @Test
    public void updateBidListTest() throws Exception {

        BidList bid = new BidList();
        bid.setAccount("account test");
        bid.setType("type test");
        bid.setBidQuantity(20d);

        BidList bidList1 = bidListService.save(bid);

        //error
        mvc.perform(post("/bidList/update/"+bidList1.getBidListId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("account", "account test")
                .param("type", "type test")
                .with(csrf())
        ).andExpect(MockMvcResultMatchers.view().name("bidList/update"));


        //ok
        mvc.perform(post("/bidList/update/"+bidList1.getBidListId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("account", "account test")
                .param("type", "type test")
                .param("bidQuantity", "40")
                .with(csrf())
        ).andExpect(redirectedUrl("/bidList/list"));

        Optional<BidList> bidList2 = bidListService.findById(bidList1.getBidListId());
        if(bidList2.isPresent()){
            Assert.assertEquals(40d, bidList2.get().getBidQuantity(), 40d);
        }

    }


}
