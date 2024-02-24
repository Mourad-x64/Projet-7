package com.nnk.springboot;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.RuleNameService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class RuleNameServiceTests {

	@Autowired
	private RuleNameRepository ruleNameRepository;
	@Autowired
	private RuleNameService ruleNameService;

	@Before
	public void cleanDb(){
		ruleNameRepository.deleteAll();
	}

	@Test
	public void ruleTest() {
		RuleName rule = new RuleName();

		rule.setName("Rule Name");
		rule.setDescription("Description");
		rule.setJson("Json");
		rule.setTemplate("Template");
		rule.setSqlStr("SQL");
		rule.setSqlPart("SQL Part");

		// Save
		rule = ruleNameService.save(rule);
		Assert.assertNotNull(rule.getRuleNameId());
		Assert.assertTrue(rule.getName().equals("Rule Name"));

		// Update
		rule.setName("Rule Name Update");
		rule = ruleNameService.save(rule);
		Assert.assertTrue(rule.getName().equals("Rule Name Update"));

		// Find
		List<RuleName> listResult = ruleNameService.findAll();
		Assert.assertTrue(listResult.size() > 0);

		// Delete
		Integer id = rule.getRuleNameId();
		ruleNameService.deleteById(id);
		Optional<RuleName> ruleList = ruleNameService.findById(id);
		Assert.assertFalse(ruleList.isPresent());
	}
}
