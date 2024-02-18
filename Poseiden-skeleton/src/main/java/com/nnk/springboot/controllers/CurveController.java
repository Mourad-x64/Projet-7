package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
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
public class CurveController {
    @Autowired
    CurvePointService curvePointService;

    @RequestMapping("/curvePoint/list")
    public String home(Model model)
    {
        List<CurvePoint> curvePoints = curvePointService.findAll();

        model.addAttribute("curvePoints", curvePoints);
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addBidForm(CurvePoint bid) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        if(result.hasErrors()){
            return "redirect:/curvePoint/add?error";
        }

        curvePointService.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Optional<CurvePoint> opt = curvePointService.findById(id);
        if(opt.isPresent()){
            model.addAttribute("curvePoint", opt.get());
        }else {
            throw new NoSuchElementException("curve point not found");
        }
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") int id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model) {
        if(result.hasErrors()){
            return "redirect:/curvePoint/update?error";
        }
        curvePoint.setCurvePointId(id);
        curvePointService.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") int id, Model model) {
        Optional<CurvePoint> opt = curvePointService.findById(id);
        if(opt.isPresent()){
            curvePointService.deleteById(id);
        }else {
            throw new IllegalArgumentException("invalid curve point id : "+id);
        }
        return "redirect:/curvePoint/list";
    }
}
