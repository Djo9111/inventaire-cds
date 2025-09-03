package com.dsi.inventairecds.controller;

import com.dsi.inventairecds.model.Equipement;
import com.dsi.inventairecds.repository.EquipementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class EquipementController {

    @Autowired
    private EquipementRepository equipementRepository;

    @GetMapping
    public String listEquipements(Model model) {
        model.addAttribute("equipements", equipementRepository.findAll());
        return "equipements";
    }

    @GetMapping("/add")
    public String addEquipementForm(Model model) {
        model.addAttribute("equipement", new Equipement());
        return "add-equipement";
    }

    @PostMapping("/add")
    public String addEquipementSubmit(@ModelAttribute Equipement equipement) {
        equipementRepository.save(equipement);
        return "redirect:/";
    }
}