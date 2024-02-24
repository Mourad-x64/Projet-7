package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;
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
public class BidListController {

    @Autowired
    BidListService bidListService;

    @RequestMapping("/bidList/list")
    public String home(Model model)
    {

        List<BidList> bidLists = bidListService.findAll();

        model.addAttribute("bidLists", bidLists);
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        if(result.hasErrors()){
            return "bidList/add";
        }

        bidListService.save(bid);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) throws Exception {
        Optional<BidList> opt = bidListService.findById(id);
        if(opt.isPresent()){
            model.addAttribute("bidList", opt.get());
        }else {
            throw new NoSuchElementException("bid list not found");
        }
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") int id, @Valid BidList bidList,
                             BindingResult result, Model model) {
        if(result.hasErrors()){
            return "bidList/update";
        }
        bidList.setBidListId(id);
        bidListService.save(bidList);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") int id, Model model) throws Exception {
        Optional<BidList> opt = bidListService.findById(id);
        if(opt.isPresent()){
            bidListService.deleteById(id);
        }else {
            throw new IllegalArgumentException("invalid bidlist id : "+id);
        }

        return "redirect:/bidList/list";
    }
}
