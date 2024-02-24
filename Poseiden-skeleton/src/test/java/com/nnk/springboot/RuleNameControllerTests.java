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
public class RuleNameControllerTests {

    @Autowired
    RuleNameRepository ruleNameRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    RuleNameService ruleNameService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mvc;

    private RuleName ruleName;

    @Before
    public void cleanDb(){
        userRepository.deleteAll();
        ruleNameRepository.deleteAll();

        User user = new User();
        user .setFullname("titi");
        user.setRole("ROLE_USER");
        user.setUsername("titi");
        user.setPassword(passwordEncoder.encode("Password_1"));

        userService.save(user);

        RuleName ruleName = new RuleName();
        ruleName.setName("test name");
        ruleName.setDescription("description test");
        ruleName.setJson("json test");
        ruleName.setTemplate("template test");
        ruleName.setSqlStr("sql str test");
        ruleName.setSqlPart("sql part test");

        this.ruleName = ruleNameService.save(ruleName);
    }

    @Test
    public void showRuleNameListTest() throws Exception {
        mvc.perform(get("/ruleName/list")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void showRuleNameAddTest() throws Exception {
        mvc.perform(get("/ruleName/add")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void showRuleNameUpdateTest() throws Exception {
        mvc.perform(get("/ruleName/update/"+ruleName.getRuleNameId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(status().isOk());

    }

    @Test
    public void deleteRuleNameTest() throws Exception {

        mvc.perform(get("/ruleName/delete/"+ruleName.getRuleNameId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .with(csrf())
        ).andExpect(redirectedUrl("/ruleName/list"));

        Optional<RuleName> listResult = ruleNameService.findById(ruleName.getRuleNameId());
        Assert.assertFalse(listResult.isPresent());

    }


    @Test
    public void addRuleNameTest() throws Exception {

        //error
        mvc.perform(post("/ruleName/validate")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("name", "test name")
                .param("description", "test name")
                .param("json", "json test")
                .param("template", "template test")
                .param("sqlStr", "sql test")
                .with(csrf())
        ).andExpect(MockMvcResultMatchers.view().name("ruleName/add"));

        //ok
        mvc.perform(post("/ruleName/validate")
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("name", "test name")
                .param("description", "test name")
                .param("json", "json test")
                .param("template", "template test")
                .param("sqlStr", "sql test")
                .param("sqlPart", "part test")
                .with(csrf())
        ).andExpect(redirectedUrl("/ruleName/list"));

        List<RuleName> listResult = ruleNameService.findAll();
        Assert.assertTrue(listResult.size() == 2);

    }

    @Test
    public void updateRuleNameTest() throws Exception {

        RuleName ruleName = new RuleName();
        ruleName.setName("test name");
        ruleName.setDescription("description test");
        ruleName.setJson("json test");
        ruleName.setTemplate("template test");
        ruleName.setSqlStr("sql str test");
        ruleName.setSqlPart("sql part test");

        RuleName ruleName1 = ruleNameService.save(ruleName);

        //error
        mvc.perform(post("/ruleName/update/"+ruleName1.getRuleNameId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("name", "test name")
                .param("description", "description test")
                .param("json", "json test")
                .param("template", "template test")
                .param("sqlStr", "sql test")
                .with(csrf())
        ).andExpect(MockMvcResultMatchers.view().name("ruleName/update"));

        //ok
        mvc.perform(post("/ruleName/update/"+ruleName1.getRuleNameId())
                .with(user("titi").password("Password_1").roles("USER","ADMIN"))
                .param("name", "test name")
                .param("description", "description test")
                .param("json", "json test")
                .param("template", "template test")
                .param("sqlStr", "sql test")
                .param("sqlPart", "testtest")
                .with(csrf())
        ).andExpect(redirectedUrl("/ruleName/list"));

        Optional<RuleName> ruleName2 = ruleNameService.findById(ruleName1.getRuleNameId());
        if(ruleName2.isPresent()){
            Assert.assertEquals("testtest", ruleName2.get().getSqlPart());
        }

    }


}
