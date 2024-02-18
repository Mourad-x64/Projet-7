package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleNameService {

    @Autowired
    RuleNameRepository ruleNameRepository;

    public List<RuleName> findAll(){
        return ruleNameRepository.findAll();
    }

    public RuleName save(RuleName ruleName){
        return ruleNameRepository.save(ruleName);
    }

    public Optional<RuleName> findById(int id){
        return ruleNameRepository.findById(id);
    }

    public void deleteById(int id){
        ruleNameRepository.deleteById(id);
    }

}
