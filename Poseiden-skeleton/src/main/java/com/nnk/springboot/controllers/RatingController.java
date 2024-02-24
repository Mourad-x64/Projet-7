package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
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
public class RatingController {

    @Autowired
    RatingService ratingService;

    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        List<Rating> ratings = ratingService.findAll();

        model.addAttribute("ratings", ratings);

        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if(result.hasErrors()){
            return "rating/add";
        }

        ratingService.save(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Optional<Rating> opt = ratingService.findById(id);
        if(opt.isPresent()){
            model.addAttribute("rating", opt.get());
        }else {
            throw new NoSuchElementException("rating not found");
        }
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") int id, @Valid Rating rating,
                             BindingResult result, Model model) {
        if(result.hasErrors()){
            return "rating/update";
        }
        rating.setRatingId(id);
        ratingService.save(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") int id, Model model) {
        Optional<Rating> opt = ratingService.findById(id);
        if(opt.isPresent()){
            ratingService.deleteById(id);
        }else {
            throw new IllegalArgumentException("invalid rating id : "+id);
        }
        return "redirect:/rating/list";
    }
}
