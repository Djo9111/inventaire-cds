package com.dsi.inventairecds.controller;

import com.dsi.inventairecds.model.Equipement;
import com.dsi.inventairecds.repository.EquipementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // NOUVEAU: Endpoint pour afficher le formulaire de modification
    @GetMapping("/edit/{id}")
    public String editEquipementForm(@PathVariable("id") Long id, Model model) {
        Equipement equipement = equipementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid equipment Id:" + id));
        model.addAttribute("equipement", equipement);
        return "edit-equipement";
    }

    // NOUVEAU: Endpoint pour traiter la soumission du formulaire de modification
    @PostMapping("/edit/{id}")
    public String editEquipementSubmit(@PathVariable("id") Long id, @ModelAttribute Equipement equipement) {
        equipement.setId(id); // Assurez-vous que l'ID est bien défini
        equipementRepository.save(equipement);
        return "redirect:/";
    }

    // NOUVEAU: Endpoint pour la suppression
    @GetMapping("/delete/{id}")
    public String deleteEquipement(@PathVariable("id") Long id) {
        equipementRepository.deleteById(id);
        return "redirect:/";
    }
}
