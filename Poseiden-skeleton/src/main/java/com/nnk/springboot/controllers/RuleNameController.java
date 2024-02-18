package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

//import javax.validation.Valid;

@Controller
public class RuleNameController {
    @Autowired
    RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {
        List<RuleName> ruleNames = ruleNameService.findAll();

        model.addAttribute("ruleNames", ruleNames);
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName bid) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if(result.hasErrors()){
            return "redirect:/ruleName/add?error";
        }

        ruleNameService.save(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Optional<RuleName> opt = ruleNameService.findById(id);
        if(opt.isPresent()){
            model.addAttribute("ruleName", opt.get());
        }else {
            throw new NoSuchElementException("Rule name not found");
        }
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") int id, @Valid RuleName ruleName,
                             BindingResult result, Model model) {
        if(result.hasErrors()){
            return "redirect:/ruleName/update?error";
        }
        ruleName.setRuleNameId(id);
        ruleNameService.save(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") int id, Model model) {
        Optional<RuleName> opt = ruleNameService.findById(id);
        if(opt.isPresent()){
            ruleNameService.deleteById(id);
        }else {
            throw new IllegalArgumentException("invalid ruleName id : "+id);
        }
        return "redirect:/ruleName/list";
    }
}
