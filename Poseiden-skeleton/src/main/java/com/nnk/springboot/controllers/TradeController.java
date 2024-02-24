package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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
public class TradeController {
    @Autowired
    TradeService tradeService;

    @RequestMapping("/trade/list")
    public String home(Model model)
    {
        List<Trade> trades = tradeService.findAll();

        model.addAttribute("trades", trades);
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(Trade bid) {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if(result.hasErrors()){
            return "trade/add";
        }

        tradeService.save(trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Optional<Trade> opt = tradeService.findById(id);
        if(opt.isPresent()){
            model.addAttribute("trade", opt.get());
        }else {
            throw new NoSuchElementException("trade not found");
        }
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") int id, @Valid Trade trade,
                             BindingResult result, Model model) {
        if(result.hasErrors()){
            return "trade/update";
        }
        trade.setTradeId(id);
        tradeService.save(trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") int id, Model model) {
        Optional<Trade> opt = tradeService.findById(id);
        if(opt.isPresent()){
            tradeService.deleteById(id);
        }else {
            throw new IllegalArgumentException("invalid trade id : "+id);
        }
        return "redirect:/trade/list";
    }
}
